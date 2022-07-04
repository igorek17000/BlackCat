package com.catfast.safetool.basic

import android.animation.ValueAnimator
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catfast.safetool.basic.exts.commonValueAnimator

open class BasicVm : ViewModel() {

    val goNext = MutableLiveData<Boolean>()
    val animatorValue = MutableLiveData<Int>()
    private var valueAnimator: ValueAnimator? = null

    fun startValueAnimator(start: Int = 0, end: Int = 100, mDuration: Long = 10000L, onUpdate: () -> Unit = {}, onEnd: () -> Unit = {}) {
        cancelValueAnimator()
        valueAnimator = commonValueAnimator(start, end, mDuration, onValue = {
            animatorValue.postValue(it)
            onUpdate.invoke()
        }, onEnd = onEnd)
        valueAnimator?.start()
    }

    fun cancelValueAnimator() {
        valueAnimator?.cancel()
        valueAnimator = null
    }

}