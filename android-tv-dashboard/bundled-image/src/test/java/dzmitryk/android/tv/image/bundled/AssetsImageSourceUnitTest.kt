package dzmitryk.android.tv.image.bundled

import androidx.compose.ui.graphics.ImageBitmap
import dzmitryk.android.tv.contracts.ImageSource
import io.mockk.mockk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.InputStream
import kotlin.time.Duration.Companion.milliseconds

/**
 */
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