package com.github.dzkoirn.hellodreamservice.demo

import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.compose.ui.platform.ComposeView
import com.github.dzkoirn.hellodreamservice.R
import com.github.dzkoirn.hellodreamservice.model.loadBackgroundImage
import com.github.dzkoirn.hellodreamservice.ui.DreamScreenContent
import java.util.Arrays

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hello_daydream)
        val list = this.assets.list("")
        Log.d("DemoActivity", "onCreate: ${Arrays.deepToString(list)}")
        findViewById<ComposeView>(R.id.compose_view).setContent {
            DreamScreenContent(loadBackgroundImage(this.assets))
        }
    }
}