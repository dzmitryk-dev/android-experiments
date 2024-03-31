package dzmitryk.android.tv.contracts

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.Flow

interface ImageSource {
    val image: Flow<ImageBitmap>
}