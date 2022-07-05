package com.github.shadowsocks.aidl

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TrafficStats(
    // Bytes per second
    var txRate: Long = 0L,
    var rxRate: Long = 0L,

    // Bytes for the current session
    var txTotal: Long = 0L,
    var rxTotal: Long = 0L
) : Parcelable {
    operator fun plus(other: TrafficStats) = TrafficStats(
        txRate + other.txRate, rxRate + other.rxRate,
        txTotal + other.txTotal, rxTotal + other.rxTotal
    )
}
