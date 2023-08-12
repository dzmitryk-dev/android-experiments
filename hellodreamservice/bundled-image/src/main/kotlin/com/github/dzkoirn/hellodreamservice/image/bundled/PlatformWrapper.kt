package com.github.dzkoirn.hellodreamservice.image.bundled

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.InputStream

internal interface PlatformWrapper {
    fun decodeStream(ins: InputStream): ImageBitmap

    fun assetsList(path: String): Array<out String>?

    fun openAsset(fileName: String): InputStream
}

internal class DefaultPlatformWrapper(
    private val assetManager: AssetManager
) : PlatformWrapper {
    override fun decodeStream(ins: InputStream): ImageBitmap =
        BitmapFactory.decodeStream(ins).asImageBitmap()

    override fun assetsList(path: String): Array<out String>? =
        assetManager.list(path)

    override fun openAsset(fileName: String): InputStream =
        assetManager.open(fileName)
}