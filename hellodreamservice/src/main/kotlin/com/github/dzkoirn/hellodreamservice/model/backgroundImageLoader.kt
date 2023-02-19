package com.github.dzkoirn.hellodreamservice.model

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.BufferedInputStream
import kotlin.RuntimeException
import kotlin.random.Random

fun loadBackgroundImage(assetManager: AssetManager): ImageBitmap {
    val imageList = assetManager.list("")?.filter { it.endsWith(".jpg") }
        ?: throw RuntimeException("Can't receive list of assets")

    val fileName = Random(System.currentTimeMillis()).nextInt(imageList.size).let {
        imageList[it]
    }
    return assetManager.open(fileName).use {
        BitmapFactory.decodeStream(BufferedInputStream(it)).asImageBitmap()
    }
}