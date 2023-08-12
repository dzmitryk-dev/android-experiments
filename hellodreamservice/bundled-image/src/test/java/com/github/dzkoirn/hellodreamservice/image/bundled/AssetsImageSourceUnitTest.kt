package com.github.dzkoirn.hellodreamservice.image.bundled

import android.content.res.AssetManager
import androidx.compose.ui.graphics.ImageBitmap
import com.github.dzkoirn.comgithubdzkoirnhellodreamservice.contracts.ImageSource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import java.io.InputStream
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AssetsImageSourceUnitTest {

    private class FakePlatformWrapper : PlatformWrapper {
        override fun decodeStream(ins: InputStream): ImageBitmap =
            mockk(relaxed = true)

        override fun assetsList(path: String): Array<out String>? =
            arrayOf("file1.jpg", "file2.jpg", "file3.jpg")

        override fun openAsset(fileName: String): InputStream =
            mockk(relaxed = true)

    }

    private val testImageSource: ImageSource = AssetsImageSource(
        platformWrapper = FakePlatformWrapper(),
        config = AssetsImageSource.AssetsImageSourceConfig(interval = 100.milliseconds)
    )

    @Test
    fun test() = runTest {
        assertEquals(5, testImageSource.image.take(5).toList().size)
    }
}