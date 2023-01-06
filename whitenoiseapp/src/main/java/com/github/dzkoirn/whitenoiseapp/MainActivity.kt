package com.github.dzkoirn.whitenoiseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

    private lateinit var surfaceView: SurfaceView
    private lateinit var renderer: Renderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        surfaceView = findViewById(R.id.surface)

        renderer = Renderer(lifecycleScope)

        surfaceView.holder.addCallback(renderer)
    }

    override fun onDestroy() {
        super.onDestroy()
        surfaceView.holder.removeCallback(renderer)
    }
}