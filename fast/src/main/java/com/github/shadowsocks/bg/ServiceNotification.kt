package com.github.shadowsocks.bg

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.PowerManager
import android.text.format.Formatter
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksServiceCallback
import com.github.shadowsocks.aidl.TrafficStats
import com.github.shadowsocks.core.R

class ServiceNotification(
    private val service: BaseService.Interface, profileName: String,
    channel: String, visible: Boolean = false
) : BroadcastReceiver() {
    private val callback: IShadowsocksServiceCallback by lazy {
        object : IShadowsocksServiceCallback.Stub() {
            override fun stateChanged(state: Int, profileName: String?, msg: String?) {}   // ignore
            override fun trafficUpdated(profileId: Long, stats: TrafficStats) {
                if (profileId != 0L) return
                builder.apply {
                    setContentText(
                        (service as Context).getString(
                            R.string.traffic,
                            service.getString(R.string.speed, Formatter.formatFileSize(service, stats.txRate)),
                            service.getString(R.string.speed, Formatter.formatFileSize(service, stats.rxRate))
                        )
                    )
                    setSubText(
                        service.getString(
                            R.string.traffic,
                            Formatter.formatFileSize(service, stats.txTotal),
                            Formatter.formatFileSize(service, stats.rxTotal)
                        )
                    )
                }
                show()
            }

            override fun trafficPersisted(profileId: Long) {}
        }
    }
    private var callbackRegistered = false

    private val builder = NotificationCompat.Builder(service as Context, channel)
        .setWhen(0)
        .setColor(ContextCompat.getColor(service, R.color.colorNotification))
        .setTicker(service.getString(R.string.forward_success))
        .setContentTitle(profileName)
        .setContentIntent(Core.configureIntent(service))
        .setSmallIcon(R.drawable.logo)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .setPriority(if (visible) NotificationCompat.PRIORITY_LOW else NotificationCompat.PRIORITY_MIN)

    init {
        service as Context
        updateCallback(service.getSystemService<PowerManager>()?.isInteractive != false)
        service.registerReceiver(this, IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        })
        show()
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (service.data.state == BaseService.State.Connected) updateCallback(intent.action == Intent.ACTION_SCREEN_ON)
    }

    private fun updateCallback(screenOn: Boolean) {
        if (screenOn) {
            service.data.binder.registerCallback(callback)
            service.data.binder.startListeningForBandwidth(callback, 1000)
            callbackRegistered = true
        } else if (callbackRegistered) {
            service.data.binder.unregisterCallback(callback)
            callbackRegistered = false
        }
    }

    private fun show() = (service as Service).startForeground(1, builder.build())

    fun destroy() {
        (service as Service).unregisterReceiver(this)
        updateCallback(false)
        service.stopForeground(true)
    }
}
