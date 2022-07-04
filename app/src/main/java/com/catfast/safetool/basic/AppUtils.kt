package com.catfast.safetool.basic

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.catfast.safetool.ui.WelcomeActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppUtils {

    private var apps = 0
    private var appTask: Job? = null
    private var update = false

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(ToolsActivityLifecycleCallbacks())
    }

    private inner class ToolsActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

        override fun onActivityStarted(activity: Activity) {
            appTask?.cancel()
            ++apps
            if (update) ActivityUtils.startActivity(WelcomeActivity::class.java)
            update = false
        }

        override fun onActivityStopped(activity: Activity) {
            if (--apps <= 0) {
                appTask = GlobalScope.launch {
                    delay(3000L)
                    update = true
                    ActivityUtils.finishActivity(WelcomeActivity::class.java)
                }
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {}

    }
}