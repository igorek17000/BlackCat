package com.catfast.safetool.basic

import android.app.Application
import com.catfast.safetool.basic.exts.app
import com.catfast.safetool.basic.exts.isMain
import com.catfast.safetool.basic.exts.suffixProcessWeb
import com.catfast.safetool.ui.MainActivity
import com.github.shadowsocks.Core
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class CatApp : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
        suffixProcessWeb()
        Core.init(this, MainActivity::class)
        if (!isMain) return
        AppUtils().init(this)
//        Firebase.initialize(this)
        ServersObtainer.run {
            initFasters()
            initConns()
        }
    }

}