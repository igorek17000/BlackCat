package com.catfast.safetool.ui

import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import com.catfast.safetool.R
import com.catfast.safetool.basic.BasicView
import com.catfast.safetool.basic.exts.immersiveStatusBar
import com.catfast.safetool.basic.exts.rotate
import com.catfast.safetool.basic.exts.roundCorner
import com.catfast.safetool.bean.ConnState
import com.catfast.safetool.databinding.ActivityMainBinding
import com.catfast.safetool.model.MainVm
import com.yarolegovich.slidingrootnav.SlideGravity
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder

class MainActivity : BasicView<ActivityMainBinding>() {

    override val vb: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainVm by viewModels()
    private val sliding by lazy { initSliding() }

    override fun basicClicks() {
        vb.imgConn.setOnClickListener {
            if (sliding.isMenuOpened) sliding.closeMenu()
            changeUiByConnState(ConnState.values().random())
        }
        vb.btnSet.setOnClickListener {
            sliding.openMenu()
        }
        vb.bg.setOnClickListener {
            if (sliding.isMenuOpened) sliding.closeMenu()
        }
        vb.btnServers.setOnClickListener {
            if (sliding.isMenuOpened) sliding.closeMenu()
        }
        vb.btnConn.setOnClickListener {
            if (sliding.isMenuOpened) sliding.closeMenu()
        }
        sliding.layout.findViewById<AppCompatTextView>(R.id.btnContact).setOnClickListener {

        }
        sliding.layout.findViewById<AppCompatTextView>(R.id.btnPrivacy).setOnClickListener {

        }
        sliding.layout.findViewById<AppCompatTextView>(R.id.btnShare).setOnClickListener {

        }
        sliding.layout.findViewById<AppCompatTextView>(R.id.btnUpgrade).setOnClickListener {

        }

    }

    override fun basicObservers() {
        immersiveStatusBar(vb.appbarLayout)
    }

    override fun basicRunners() {

    }

    private fun initSliding(): SlidingRootNav {
        return SlidingRootNavBuilder(this)
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

            }
            ConnState.OPENING -> {
                vb.animationView.visibility = View.VISIBLE
                vb.animationView.playAnimation()
                vb.imgSwitch.startAnimation(rotate(1000L))

            }
            ConnState.CLOSING -> {
                vb.animationView.visibility = View.VISIBLE
                vb.animationView.playAnimation()
                vb.imgSwitch.startAnimation(rotate(1000L))
            }
            ConnState.END -> {
                vb.imgConn.setImageResource(R.mipmap.ic_disconn)
                vb.animationView.visibility = View.GONE
                vb.animationView.pauseAnimation()
                vb.imgSwitch.clearAnimation()
            }
        }
    }

}