package com.catfast.safetool.basic

import com.catfast.safetool.basic.exts.createFlow
import com.catfast.safetool.basic.exts.format
import com.catfast.safetool.basic.exts.openTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*

object TimeDuration {

    fun updateOpenTime() {
        openTime = Date().time
    }

    fun getOpenTimeForStr(): String {
        val seconds = (Date().time - openTime) / 1000L
        val hh = format(seconds / 3600)
        val mm = format(seconds % 3600 / 60)
        val ss = format(seconds % 60)
        return "$hh:$mm:$ss"
    }

    private var openJob: Job? = null

    fun startJob(onStrBack: (String) -> Unit) {
        cancelJob()
        openJob = GlobalScope.launch(Dispatchers.Main) {
            createFlow(1000L, 1000L)
                .flowOn(Dispatchers.IO)
                .collect {
                    onStrBack.invoke(getOpenTimeForStr())
                }
        }
    }

    fun cancelJob() {
        openJob?.cancel()
        openJob = null
    }


}