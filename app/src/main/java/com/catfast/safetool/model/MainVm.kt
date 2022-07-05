package com.catfast.safetool.model

import android.app.Activity
import android.net.VpnService
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.catfast.safetool.basic.BasicVm
import com.catfast.safetool.basic.ServersObtainer
import com.catfast.safetool.basic.TimeDuration
import com.catfast.safetool.basic.exts.curState
import com.catfast.safetool.basic.exts.isStateConnected
import com.catfast.safetool.bean.ConnState
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService

class MainVm : BasicVm(), ShadowsocksConnection.Callback {

    val connStateData = MutableLiveData<ConnState>()
    val connInfoData = MutableLiveData<Boolean>()

    private var mTimer: CountDownTimer? = null
    private val connection = ShadowsocksConnection(true)

    fun initConn(activity: Activity) {
        connection.connect(activity, this)
    }

    fun cancelConn(activity: Activity) {
        cancelTimer()
        connection.disconnect(activity)
    }

    fun secureSwitch(activity: Activity) {
        getSecurityPermission(activity)
    }

    private fun openSwitch(activity: Activity) {
        if (isStateConnected) {
            connStateData.postValue(ConnState.CLOSING)
            startTimer(activity, false)
            Core.stopService()
        } else {
            connStateData.postValue(ConnState.OPENING)
            startTimer(activity, true)
            ServersObtainer.switchOn()
        }
    }

    fun onSecurePermit(activity: Activity) {
        openSwitch(activity)
    }

    private fun getSecurityPermission(activity: Activity) {
        val secureIntent = VpnService.prepare(activity)
        if (secureIntent == null) openSwitch(activity)
        else activity.startActivityForResult(secureIntent, 9287, null)
    }

    private fun startTimer(activity: Activity, isOpen: Boolean) {
        cancelTimer()
        mTimer = object : CountDownTimer(2500L, 300L) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                refreshUi()
                goNext.postValue(true)
            }
        }
        mTimer?.start()
    }

    fun cancelTimer() {
        mTimer?.cancel()
        mTimer = null
    }

    fun refreshUi() {
        if (isStateConnected) {
            TimeDuration.updateOpenTime()
            connStateData.postValue(ConnState.END)
        } else connStateData.postValue(ConnState.START)
    }

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        curState = state
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        curState = try {
            BaseService.State.values()[service.state]
        } catch (ignore: Exception) {
            BaseService.State.Idle
        }
        connStateData.postValue(if (isStateConnected) ConnState.END else ConnState.START)
    }


}