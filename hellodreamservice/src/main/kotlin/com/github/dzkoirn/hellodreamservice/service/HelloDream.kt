package com.github.dzkoirn.hellodreamservice.service

import android.service.dreams.DreamService
import androidx.compose.ui.platform.ComposeView
import com.github.dzkoirn.hellodreamservice.R
import com.github.dzkoirn.hellodreamservice.ui.DreamScreenContent

class HelloDream : DreamService() {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        isFullscreen = true
        isInteractive = false

        setContentView(R.layout.hello_daydream)
        findViewById<ComposeView>(R.id.compose_view).setContent {
//            DreamScreenContent()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }
}