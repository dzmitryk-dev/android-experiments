package com.github.dzkoirn.hellodreamservice.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.compose.ui.platform.ComposeView
import com.github.dzkoirn.hellodreamservice.model.loadBackgroundImage
import com.github.dzkoirn.hellodreamservice.ui.DreamScreenContent
import java.util.Arrays

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = this.assets.list("")
        Log.d("DemoActivity", "onCreate: ${Arrays.deepToString(list)}")
        setContentView(ComposeView(this).apply {
            setContent {
                DreamScreenContent(loadBackgroundImage(this@DemoActivity.assets))
                // SimpleTestComposabe()
            }
        })
    }
}