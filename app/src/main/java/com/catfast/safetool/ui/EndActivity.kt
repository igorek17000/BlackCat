package com.catfast.safetool.ui

import android.graphics.Color
import com.catfast.safetool.R
import com.catfast.safetool.basic.BasicView
import com.catfast.safetool.basic.TimeDuration
import com.catfast.safetool.basic.exts.getConnImg
import com.catfast.safetool.basic.exts.getConnName
import com.catfast.safetool.basic.exts.immersiveStatusBar
import com.catfast.safetool.basic.exts.isStateConnected
import com.catfast.safetool.databinding.ActivityEndBinding
import com.github.shadowsocks.Core

class EndActivity : BasicView<ActivityEndBinding>() {

    override val vb: ActivityEndBinding by lazy { ActivityEndBinding.inflate(layoutInflater) }

    override fun basicClicks() {
        vb.btnBack.setOnClickListener { onBackPressed() }
    }

    override fun basicObservers() {

    }

    override fun basicRunners() {
        immersiveStatusBar(vb.appbarLayout)
        if (isStateConnected) {
            vb.imgState.setImageResource(R.mipmap.ic_end_conn)
            vb.connTime.setTextColor(Color.parseColor("#00AC18"))
            TimeDuration.startJob {
                vb.connTime.text = it
            }
        } else {
            vb.imgState.setImageResource(R.mipmap.ic_end_disconn)
            vb.connTime.setTextColor(Color.parseColor("#B8B8B8"))
            vb.connTime.text = TimeDuration.getOpenTimeForStr()
        }
        Core.currentProfile?.main?.let {
            vb.imgServer.setImageResource(getConnImg(it.catName ?: ""))
            vb.serverName.text = getConnName(it.catName, it.catCity)
        }
    }

    override fun onDestroy() {
        TimeDuration.cancelJob()
        super.onDestroy()
    }
}