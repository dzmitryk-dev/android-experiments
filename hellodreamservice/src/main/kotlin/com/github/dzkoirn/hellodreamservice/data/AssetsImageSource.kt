package com.github.dzkoirn.hellodreamservice.data

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.BufferedInputStream
import kotlin.random.Random

class AssetsImageSource(
    private val assetManager: AssetManager
) : ImageSource {
    override suspend fun loadImage(): ImageBitmap {
        val imageList = assetManager.list("")?.filter { it.endsWith(".jpg") }
            ?: throw RuntimeException("Can't receive list of assets")

        val fileName = Random(System.currentTimeMillis()).nextInt(imageList.size).let {
            imageList[it]
        }
        return assetManager.open(fileName).use {
            BitmapFactory.decodeStream(BufferedInputStream(it)).asImageBitmap()
        }
    }
}