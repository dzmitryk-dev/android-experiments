package com.github.dzkoirn.hellodreamservice.service

import android.service.dreams.DreamService
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.github.dzkoirn.hellodreamservice.model.loadBackgroundImage
import com.github.dzkoirn.hellodreamservice.ui.DreamScreenContent
import com.github.dzkoirn.hellodreamservice.ui.DummyLifeCycleOwner

class HelloDreamService : DreamService() {

    // hack from https://gist.github.com/handstandsam/6ecff2f39da72c0b38c07aa80bbb5a2f?permalink_comment_id=4016921&utm_source=pocket_saves
    // Trick The ComposeView into thinking we are tracking lifecycle
    private val viewModelStore = ViewModelStore()
    private val lifecycleOwner = DummyLifeCycleOwner()

    override fun onCreate() {
        super.onCreate()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        isFullscreen = true
        isInteractive = false

        val composeView = ComposeView(this).apply {
            setContent {
                DreamScreenContent(loadBackgroundImage(this@HelloDreamService.assets))
            }
        }

        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
        ViewTreeLifecycleOwner.set(composeView, lifecycleOwner)
        ViewTreeViewModelStoreOwner.set(composeView) { viewModelStore }
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)

        setContentView(composeView)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }
}