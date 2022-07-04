package com.catfast.safetool.basic

import android.app.Application
import com.catfast.safetool.basic.exts.app

class CatApp : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
    }

}