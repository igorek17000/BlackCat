package com.catfast.safetool.ui

import androidx.activity.viewModels
import com.catfast.safetool.basic.BasicVm
import com.catfast.safetool.basic.BasicView
import com.catfast.safetool.basic.exts.goMain
import com.catfast.safetool.basic.exts.immersiveStatusBar
import com.catfast.safetool.databinding.ActivityWelcomeBinding

class WelcomeActivity : BasicView<ActivityWelcomeBinding>() {

    override val vb: ActivityWelcomeBinding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }
    private val viewModel: BasicVm by viewModels()

    override fun basicObservers() {
        viewModel.animatorValue.observe(this) { vb.progressbar.progress = it }
        viewModel.goNext.observe(this) { goMain() }
    }

    override fun basicRunners() {
        immersiveStatusBar()
        startProgress()
    }

    private fun startProgress() {
        viewModel.startValueAnimator(0, 100, 2000L, onUpdate = {
        }, onEnd = { viewModel.goNext.postValue(true) })
    }

    private fun maxProgress(block: () -> Unit) {
        viewModel.cancelValueAnimator()
        val delay = (100 - vb.progressbar.progress) * 10L
        viewModel.startValueAnimator(vb.progressbar.progress, 100, delay, onEnd = block)
    }

    override fun onDestroy() {
        viewModel.cancelValueAnimator()
        super.onDestroy()
    }


}