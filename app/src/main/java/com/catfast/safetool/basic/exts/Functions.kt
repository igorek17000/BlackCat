package com.catfast.safetool.basic.exts

import android.animation.ValueAnimator
import android.app.Application
import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.webkit.WebView
import androidx.annotation.Keep
import com.blankj.utilcode.util.ProcessUtils
import com.catfast.safetool.BuildConfig
import com.catfast.safetool.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.text.DecimalFormat

fun commonValueAnimator(start: Int, end: Int, mDuration: Long, onValue: (Int) -> Unit, onEnd: () -> Unit = {}): ValueAnimator {
    val animator = ValueAnimator.ofInt(start, end)
    animator.apply {
        duration = mDuration
        interpolator = LinearInterpolator()
        addUpdateListener {
            (it.animatedValue as? Int)?.let { value ->
                onValue.invoke(value)
                if (end == value) onEnd.invoke()
            }
        }
    }
    return animator
}

fun rotate(time: Long) = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
    duration = time
    repeatCount = Animation.INFINITE
    interpolator = LinearInterpolator()
    fillAfter = false
}

fun createFlow(startDelay: Long = 0L, interval: Long = 1000L) = flow {
    delay(startDelay)
    while (true) {
        emit(Unit)
        delay(interval)
    }
}

fun format(number: Long): String {
    return DecimalFormat("00").format(number)
}

fun View.roundCorner(context: Context, radius: Float) {
    this.clipToOutline = true
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius.dp2Px(context))
        }
    }
}

fun Int.dp2Px(context: Context) = (context.resources.displayMetrics.density * this)
fun Float.dp2Px(context: Context) = (context.resources.displayMetrics.density * this)


val isSDK28: Boolean
    get() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

val isMain: Boolean
    get() {
        return BuildConfig.APPLICATION_ID == if (isSDK28) Application.getProcessName() else ProcessUtils.getCurrentProcessName()
    }

fun suffixProcessWeb() {
    if (isSDK28) {
        Application.getProcessName().let {
            if (BuildConfig.APPLICATION_ID != it) WebView.setDataDirectorySuffix(it)
        }
    }
}

@Keep
fun getConnImg(catName: String): Int {
    return when (catName.lowercase()) {
        "United States".lowercase() -> R.mipmap.unitedstates
        "Canada".lowercase() -> R.mipmap.canada
        "United Kingdom".lowercase() -> R.mipmap.unitedkingdom
        "Australia".lowercase() -> R.mipmap.australia
        "Singapore".lowercase() -> R.mipmap.singapore
        "Japan".lowercase() -> R.mipmap.japan
        "Germany".lowercase() -> R.mipmap.germany
        "France".lowercase() -> R.mipmap.france
        else -> R.drawable.ic_fastcat
    }
}

@Keep
fun getConnName(catName: String?, catCity: String?): String {
    return if (catCity.isNullOrEmpty()) catName ?: "" else "${catName ?: ""} (${catCity})"
}

