package com.catfast.safetool.basic.exts

import com.catfast.safetool.basic.CatApp
import com.github.shadowsocks.bg.BaseService
import java.util.*

lateinit var app: CatApp
var openTime = Date().time
var curState = BaseService.State.Idle
val isStateConnected: Boolean
    get() {
        return BaseService.State.Connected == curState
    }