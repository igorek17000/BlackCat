package com.catfast.safetool.ui

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.lifecycleScope
import com.catfast.safetool.R
import com.catfast.safetool.basic.BasicView
import com.catfast.safetool.basic.ServersObtainer
import com.catfast.safetool.basic.exts.*
import com.catfast.safetool.bean.ConnState
import com.catfast.safetool.databinding.ActivityMainBinding
import com.catfast.safetool.model.MainVm
import com.yarolegovich.slidingrootnav.SlideGravity
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BasicView<ActivityMainBinding>() {

    override val vb: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainVm by viewModels()
    private lateinit var sliding: SlidingRootNav

    override fun basicClicks() {
        initSliding()
        vb.imgConn.setOnClickListener {
            if (sliding.isMenuOpened) sliding.closeMenu()
            viewModel.secureSwitch(activity)
        }
        vb.btnSet.setOnClickListener {
            if (vb.btnConn.isEnabled.not()) return@setOnClickListener
            sliding.openMenu()
        }
        vb.bg.setOnClickListener {
            if (sliding.isMenuOpened) sliding.closeMenu()
        }
        vb.btnServers.setOnClickListener {
            if (sliding.isMenuOpened) sliding.closeMenu()
            if (vb.btnConn.isEnabled.not()) return@setOnClickListener
            startActivityForResult(Intent(activity, ServersActivity::class.java), 9288, null)
        }
        vb.btnConn.setOnClickListener {
            if (sliding.isMenuOpened) sliding.closeMenu()
            viewModel.secureSwitch(activity)
        }
        sliding.layout.findViewById<AppCompatTextView>(R.id.btnContact).setOnClickListener {
            contact()
        }
        sliding.layout.findViewById<AppCompatTextView>(R.id.btnPrivacy).setOnClickListener {
            watchPrivacy()
        }
        sliding.layout.findViewById<AppCompatTextView>(R.id.btnShare).setOnClickListener {
            share()
        }
        sliding.layout.findViewById<AppCompatTextView>(R.id.btnUpgrade).setOnClickListener {
            upgrade()
        }
    }

    override fun basicObservers() {
        viewModel.connStateData.observe(this) { changeUiByConnState(it) }
        viewModel.goNext.observe(this) {
            lifecycleScope.launch {
                while (!isViewResume) delay(200L)
                startActivity(Intent(activity, EndActivity::class.java))
            }
        }
        viewModel.connInfoData.observe(this) {
            runCatching {
                val connItem = ServersObtainer.conns.first { item -> item.selected }
                vb.serverName.text = connItem.name
                vb.imgServer.setImageResource(getConnImg(connItem.name))
            }
        }
    }

    override fun basicRunners() {
        immersiveStatusBar(vb.appbarLayout)
        viewModel.initConn(activity)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (RESULT_OK == resultCode) {
            if (requestCode == 9287) viewModel.onSecurePermit(activity)
            else if (requestCode == 9288) {
                if (data?.getBooleanExtra("isChangedConn", false) == true) viewModel.secureSwitch(activity)
                else if (isStateConnected) viewModel.secureSwitch(activity)
                viewModel.connInfoData.postValue(true)
            }
        }
    }

    private fun initSliding() {
        sliding = SlidingRootNavBuilder(this)
            .withGravity(SlideGravity.LEFT)
            .withDragDistance(258)
            .withRootViewScale(0.85f)
            .withRootViewElevation(10)
            .withMenuLayout(R.layout.layout_set)
            .addDragListener {
                vb.bg.roundCorner(activity, 36 * it)
            }
            .inject()
    }

    private fun changeUiByConnState(connState: ConnState) {
        vb.btnConn.setBackgroundResource(connState.btnRes)
        vb.imgSwitch.setImageResource(connState.switchRes)
        when (connState) {
            ConnState.START -> {
                vb.imgConn.setImageResource(R.mipmap.ic_no_conn)
                vb.animationView.visibility = View.GONE
                vb.animationView.pauseAnimation()
                vb.imgSwitch.clearAnimation()
                vb.imgConn.isEnabled = true
                vb.btnConn.isEnabled = true
            }
            ConnState.OPENING -> {
                vb.animationView.visibility = View.VISIBLE
                vb.animationView.playAnimation()
                vb.imgSwitch.startAnimation(rotate(1000L))
                vb.imgConn.isEnabled = false
                vb.btnConn.isEnabled = false
            }
            ConnState.CLOSING -> {
                vb.animationView.visibility = View.VISIBLE
                vb.animationView.playAnimation()
                vb.imgSwitch.startAnimation(rotate(1000L))
                vb.imgConn.isEnabled = false
                vb.btnConn.isEnabled = false
            }
            ConnState.END -> {
                vb.imgConn.setImageResource(R.mipmap.ic_disconn)
                vb.animationView.visibility = View.GONE
                vb.animationView.pauseAnimation()
                vb.imgSwitch.clearAnimation()
                vb.imgConn.isEnabled = true
                vb.btnConn.isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        viewModel.cancelConn(activity)
        super.onDestroy()
    }

}