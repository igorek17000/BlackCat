package com.github.shadowsocks.bg

import android.os.Build
import android.os.SystemClock
import android.system.ErrnoException
import android.system.Os
import android.system.OsConstants
import androidx.annotation.MainThread
import com.github.shadowsocks.Core
import com.github.shadowsocks.utils.Commandline
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.io.InputStream
import kotlin.concurrent.thread

class GuardedProcessPool(private val onFatal: suspend (IOException) -> Unit) : CoroutineScope {
    companion object {
        private val pid by lazy {
            Class.forName("java.lang.ProcessManager\$ProcessImpl").getDeclaredField("pid").apply { isAccessible = true }
        }
    }

    private inner class Guard(private val cmd: List<String>) {
        private lateinit var process: Process

        private fun streamLogger(input: InputStream, logger: (String) -> Unit) = try {
            input.bufferedReader().forEachLine(logger)
        } catch (_: IOException) {
        }    // ignore

        fun start() {
            process = ProcessBuilder(cmd).directory(Core.deviceStorage.noBackupFilesDir).start()
        }

        suspend fun looper(onRestartCallback: (suspend () -> Unit)?) {
            var running = true
            val cmdName = File(cmd.first()).nameWithoutExtension
            val exitChannel = Channel<Int>()
            try {
                while (true) {
                    thread(name = "stderr-$cmdName") {
                        streamLogger(process.errorStream) { Timber.tag(cmdName).e(it) }
                    }
                    thread(name = "stdout-$cmdName") {
                        streamLogger(process.inputStream) { Timber.tag(cmdName).v(it) }
                        runBlocking { exitChannel.send(process.waitFor()) }
                    }
                    val startTime = SystemClock.elapsedRealtime()
                    val exitCode = exitChannel.receive()
                    running = false
                    when {
                        SystemClock.elapsedRealtime() - startTime < 1000 -> throw IOException(
                            "$cmdName exits too fast (exit code: $exitCode)"
                        )
                        exitCode == 128 + OsConstants.SIGKILL -> Timber.w("$cmdName was killed")
                        else -> Timber.w(IOException("$cmdName unexpectedly exits with code $exitCode"))
                    }
                    Timber.i("restart process: ${Commandline.toString(cmd)} (last exit code: $exitCode)")
                    start()
                    running = true
                    onRestartCallback?.invoke()
                }
            } catch (e: IOException) {
                Timber.w("error occurred. stop guard: ${Commandline.toString(cmd)}")
                GlobalScope.launch(Dispatchers.Main) { onFatal(e) }
            } finally {
                if (running) withContext(NonCancellable) {  // clean-up cannot be cancelled
                    if (Build.VERSION.SDK_INT < 24) {
                        try {
                            Os.kill(pid.get(process) as Int, OsConstants.SIGTERM)
                        } catch (e: ErrnoException) {
                            if (e.errno != OsConstants.ESRCH) Timber.w(e)
                        } catch (e: ReflectiveOperationException) {
                            Timber.w(e)
                        }
                        if (withTimeoutOrNull(500) { exitChannel.receive() } != null) return@withContext
                    }
                    process.destroy()
                    if (Build.VERSION.SDK_INT >= 26) {
                        if (withTimeoutOrNull(1000) { exitChannel.receive() } != null) return@withContext
                        process.destroyForcibly()
                    }
                    exitChannel.receive()
                }
            }
        }
    }

    override val coroutineContext = Dispatchers.Main.immediate + Job()

    @MainThread
    fun start(cmd: List<String>, onRestartCallback: (suspend () -> Unit)? = null) {
        Timber.i("start process: ${Commandline.toString(cmd)}")
        Guard(cmd).apply {
            start()
            launch { looper(onRestartCallback) }
        }
    }

    @MainThread
    fun close(scope: CoroutineScope) {
        cancel()
        coroutineContext[Job]!!.also { job -> scope.launch { job.join() } }
    }
}
