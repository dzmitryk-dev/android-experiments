package com.github.dzkoirn.hellodreamservice.service

import android.service.dreams.DreamService
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.github.dzkoirn.hellodreamservice.data.AssetsImageSource
import com.github.dzkoirn.hellodreamservice.model.DreamModel
import com.github.dzkoirn.hellodreamservice.ui.DreamScreenContent
import com.github.dzkoirn.hellodreamservice.ui.DummyLifeCycleOwner
import com.github.dzkoirn.hellodreamservice.ui.DummyViewModelStoreOwner

class HelloDreamService : DreamService() {

    // hack from https://gist.github.com/handstandsam/6ecff2f39da72c0b38c07aa80bbb5a2f?permalink_comment_id=4016921&utm_source=pocket_saves
    // Trick The ComposeView into thinking we are tracking lifecycle
    private val lifecycleOwner = DummyLifeCycleOwner()
    private val viewModelStoreOwner = DummyViewModelStoreOwner()
    private lateinit var dreamModel: DreamModel

    override fun onCreate() {
        super.onCreate()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        val imageSource= AssetsImageSource(assets)
        dreamModel = DreamModel(imageSource)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        isFullscreen = true
        isInteractive = false

        val composeView = ComposeView(this).apply {
            setContent {
               dreamModel.wallpaper.observeAsState().value?.let {
                   DreamScreenContent(it)
               }
            }
        }

        // hack from https://gist.github.com/handstandsam/6ecff2f39da72c0b38c07aa80bbb5a2f?permalink_comment_id=4016921&utm_source=pocket_saves
        // Trick The ComposeView into thinking we are tracking lifecycle
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)

        setContentView(composeView)

        dreamModel.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        dreamModel.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}