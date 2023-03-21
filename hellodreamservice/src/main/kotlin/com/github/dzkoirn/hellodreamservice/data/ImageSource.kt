package com.github.dzkoirn.hellodreamservice.data

import androidx.compose.ui.graphics.ImageBitmap

interface ImageSource {
    suspend fun loadImage(): ImageBitmap
}