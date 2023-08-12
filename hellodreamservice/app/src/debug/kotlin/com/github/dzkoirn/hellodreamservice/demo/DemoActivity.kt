package com.github.dzkoirn.hellodreamservice.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import com.github.dzkoirn.hellodreamservice.image.bundled.AssetsImageSource
import com.github.dzkoirn.hellodreamservice.model.DreamModel
import com.github.dzkoirn.hellodreamservice.ui.DreamScreenContent
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DemoActivity : AppCompatActivity() {

    private lateinit var dreamModel: DreamModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageSource = AssetsImageSource(assets, AssetsImageSource.AssetsImageSourceConfig(1.minutes))
        dreamModel = DreamModel(imageSource, 10.0.seconds.inWholeMilliseconds)

        setContentView(ComposeView(this).apply {
            setContent {
                val wallpaper = dreamModel.wallpaper.observeAsState()
                wallpaper.value?.let {
                    DreamScreenContent(it)
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        dreamModel.start()
    }

    override fun onStop() {
        super.onStop()
        dreamModel.stop()
    }
}