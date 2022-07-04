package com.catfast.safetool.bean

import androidx.annotation.Keep
import com.catfast.safetool.R

@Keep
enum class ConnState(val switchRes: Int, val btnRes: Int) {
    START(R.drawable.ic_open, R.drawable.selector_common_r12),
    OPENING(R.drawable.ic_circle_progress, R.drawable.selector_common_r12),
    CLOSING(R.drawable.ic_circle_progress, R.drawable.selector_common_r12),
    END(R.drawable.ic_complete, R.drawable.selector_green_r12)
}