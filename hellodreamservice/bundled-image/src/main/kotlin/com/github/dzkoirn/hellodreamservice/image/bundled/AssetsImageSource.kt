package com.github.dzkoirn.hellodreamservice.image.bundled

import android.content.res.AssetManager
import androidx.compose.ui.graphics.ImageBitmap
import com.github.dzkoirn.comgithubdzkoirnhellodreamservice.contracts.ImageSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.BufferedInputStream
import kotlin.random.Random
import kotlin.time.Duration

class AssetsImageSource : ImageSource {

    private val config: AssetsImageSourceConfig
    private val platformWrapper: PlatformWrapper

    constructor(
        assetManager: AssetManager,
        config: AssetsImageSourceConfig
    ) : this(
        config, DefaultPlatformWrapper(assetManager)
    )

    internal constructor(
        config: AssetsImageSourceConfig,
        platformWrapper: PlatformWrapper
    ) {
        this.config = config
        this.platformWrapper = platformWrapper
    }

    override val image: Flow<ImageBitmap>
        get() = flow {
            val imageList = platformWrapper.assetsList("images")?.filter { it.endsWith(".jpg") }
                ?: throw RuntimeException("Can't receive list of assets")
            val random = Random(System.currentTimeMillis())

            while (true) {
                val fileName = random.nextInt(imageList.size).let {
                    imageList[it]
                }
                emit(loadImage("images/$fileName"))
                delay(config.interval)
            }
        }

    private fun loadImage(fileName: String): ImageBitmap =
        platformWrapper.openAsset(fileName).use {
            platformWrapper.decodeStream(BufferedInputStream(it))
        }

    data class AssetsImageSourceConfig(
        val interval: Duration
    )
}
