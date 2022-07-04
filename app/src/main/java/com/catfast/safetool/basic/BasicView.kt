package com.catfast.safetool.basic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.catfast.safetool.basic.exts.autoSizeHeight
import com.catfast.safetool.basic.exts.isActivityResume

abstract class BasicView<BasicVB : ViewBinding> : AppCompatActivity() {

    lateinit var activity: AppCompatActivity
    abstract val vb: BasicVB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        autoSizeHeight()
        setContentView(vb.root)
        basicClicks()
        basicObservers()
        basicRunners()
    }

    open fun basicClicks() {}
    abstract fun basicObservers()
    abstract fun basicRunners()

    val isViewResume: Boolean
        get() {
            return isActivityResume() && viewResumed
        }

    private var viewResumed = false

    override fun onResume() {
        super.onResume()
        viewResumed = true
    }

    override fun onPause() {
        viewResumed = false
        super.onPause()
    }

    override fun onStop() {
        viewResumed = false
        super.onStop()
    }


}