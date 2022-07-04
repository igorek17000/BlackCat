package com.catfast.safetool.ui

import com.catfast.safetool.basic.BasicView
import com.catfast.safetool.basic.exts.immersiveStatusBar
import com.catfast.safetool.databinding.ActivityEndBinding

class EndActivity : BasicView<ActivityEndBinding>() {

    override val vb: ActivityEndBinding by lazy { ActivityEndBinding.inflate(layoutInflater) }

    override fun basicObservers() {

    }

    override fun basicRunners() {
        immersiveStatusBar(vb.appbarLayout)
    }
}