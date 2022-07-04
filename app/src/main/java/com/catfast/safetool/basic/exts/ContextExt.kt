package com.catfast.safetool.basic.exts

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.catfast.safetool.ui.MainActivity

fun Activity.goMain() {
    if (ActivityUtils.isActivityExistsInStack(MainActivity::class.java).not()) startActivity(Intent(this, MainActivity::class.java))
    finish()
}

fun Activity.isExists(): Boolean {
    return !isFinishing && !isDestroyed
}

fun Activity.immersiveStatusBar(view: View? = null) {
    BarUtils.transparentStatusBar(this)
    BarUtils.setStatusBarLightMode(this, true)
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



