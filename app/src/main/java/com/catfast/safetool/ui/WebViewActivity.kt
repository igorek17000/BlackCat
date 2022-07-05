package com.catfast.safetool.ui

import android.webkit.WebViewClient
import com.catfast.safetool.basic.BasicView
import com.catfast.safetool.basic.exts.PRIVACY_URL
import com.catfast.safetool.basic.exts.immersiveStatusBar
import com.catfast.safetool.databinding.ActivityWebviewBinding

class WebViewActivity : BasicView<ActivityWebviewBinding>() {

    override val vb: ActivityWebviewBinding by lazy { ActivityWebviewBinding.inflate(layoutInflater) }

    override fun basicClicks() {
        vb.btnBack.setOnClickListener { onBackPressed() }
    }

    override fun basicObservers() {

    }

    override fun basicRunners() {
        immersiveStatusBar(vb.appbarLayout, false)
        vb.webview.webViewClient = WebViewClient()
        vb.webview.loadUrl(PRIVACY_URL)
    }

}