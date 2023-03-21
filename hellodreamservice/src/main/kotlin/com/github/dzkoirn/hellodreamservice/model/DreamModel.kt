package com.github.dzkoirn.hellodreamservice.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.dzkoirn.hellodreamservice.data.ImageSource
import kotlinx.coroutines.runBlocking
import java.util.Timer
import java.util.TimerTask
import kotlin.time.Duration.Companion.minutes

class DreamModel(
    private val imageSource: ImageSource,
    private val updateInterval: Long = 15.0.minutes.inWholeMilliseconds
) {
    private val timer = Timer("Update-timer")

    private val updateWallpaperTask = object : TimerTask() {
        override fun run() {
            runBlocking {
                val image = imageSource.loadImage()
                mutableWallpaper.postValue(image)
            }
        }
    }

    private val mutableWallpaper = MutableLiveData<ImageBitmap>()
    val wallpaper: LiveData<ImageBitmap>
        get() = mutableWallpaper

    fun start() {
        timer.schedule(updateWallpaperTask, 0L, updateInterval)
    }

    fun stop() {
        timer.cancel()
    }
}