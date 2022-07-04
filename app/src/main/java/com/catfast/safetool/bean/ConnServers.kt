package com.catfast.safetool.bean

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ConnItem(
    @SerializedName("catHost")
    val hostName: String = "",
    @SerializedName("catName")
    val name: String = "Super Fast Server",
    @SerializedName("catCity")
    val city: String? = "",
    @SerializedName("catWay")
    val way: String = "chacha20-ietf-poly1305",
    @SerializedName("catPort")
    val port: Int = 806,
    @SerializedName("catPass")
    val pwd: String = "G!yBwPWH3Vao",
    var selected: Boolean = false
)

