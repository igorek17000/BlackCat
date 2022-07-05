package com.catfast.safetool.basic.exts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.catfast.safetool.BuildConfig
import com.catfast.safetool.ui.MainActivity
import com.catfast.safetool.ui.WebViewActivity

fun Activity.goMain() {
    if (ActivityUtils.isActivityExistsInStack(MainActivity::class.java).not()) startActivity(Intent(this, MainActivity::class.java))
    finish()
}

fun Activity.isExists(): Boolean {
    return !isFinishing && !isDestroyed
}

fun Activity.immersiveStatusBar(view: View? = null, isLightMode: Boolean = true) {
    BarUtils.transparentStatusBar(this)
    BarUtils.setStatusBarLightMode(this, isLightMode)
    view?.let { BarUtils.addMarginTopEqualStatusBarHeight(it) }
}

fun Activity.autoSizeHeight() {
    val displayMetrics = resources.displayMetrics
    (displayMetrics.heightPixels / 760f).let {
        displayMetrics.run {
            density = it
            scaledDensity = it
            densityDpi = (it * 160f).toInt()
        }
    }
}

fun AppCompatActivity.isActivityResume(): Boolean {
    return Lifecycle.State.RESUMED == lifecycle.currentState
}

fun Context.contact() {
    runCatching {
        startActivity(Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$EMAIL")
        })
    }.onFailure {
        ToastUtils.showLong("Contact us by email $EMAIL")
    }
}

fun Context.watchPrivacy() {
    startActivity(Intent(this, WebViewActivity::class.java))
}

fun Context.share() {
    runCatching {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
        }
        startActivity(intent)
    }
}

fun Context.upgrade() {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")).apply {
            setPackage("com.android.vending")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}




