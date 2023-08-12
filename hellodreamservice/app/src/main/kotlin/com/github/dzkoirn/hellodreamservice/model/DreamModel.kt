package com.github.dzkoirn.hellodreamservice.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.dzkoirn.comgithubdzkoirnhellodreamservice.contracts.ImageSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Timer
import java.util.TimerTask
import kotlin.time.Duration.Companion.minutes

/**
 * Because we are running in the DreamService, that does not has lifecycle support out of the box,
 * this class does not extends Android Jetpack ViewModel, because it is useless here.
 * Of course we can extend ViewModel class and then bind to Lifecycle of service to emulate View lyfecycle,
 * but it looks to complicated, so here we simple create normal class and pass all necessary lifecycle events to it by hands.
 */
class DreamModel(
    private val imageSource: ImageSource,
    private val updateInterval: Long = 15.0.minutes.inWholeMilliseconds
) {
    private val modelScope = CoroutineScope(Job())


    private val mutableWallpaper = MutableLiveData<ImageBitmap>()
    val wallpaper: LiveData<ImageBitmap>
        get() = mutableWallpaper

    fun start() {
        modelScope.launch {
            imageSource.image.collect { mutableWallpaper.postValue(it) }
        }
    }

    fun stop() {
        modelScope.cancel()
    }
}