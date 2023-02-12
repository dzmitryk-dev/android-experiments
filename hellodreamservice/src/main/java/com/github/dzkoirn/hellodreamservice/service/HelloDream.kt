package com.github.dzkoirn.hellodreamservice.service

import android.service.dreams.DreamService
import com.github.dzkoirn.hellodreamservice.R

class HelloDream : DreamService() {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        isFullscreen = true
        isInteractive = false

        setContentView(R.layout.test_daydream)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }
}