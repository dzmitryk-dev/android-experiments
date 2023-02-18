package io.github.dzkoirn.androidexperiments.helloshaders

import android.content.Context
import android.opengl.GLSurfaceView


class ShaderSurfaceView(context: Context) : GLSurfaceView(context) {
    private val mRenderer: ShaderRenderer

    init {
        setEGLContextClientVersion(2)
        mRenderer = ShaderRenderer()
        setRenderer(mRenderer)

        //RENDERMODE_WHEN_DIRTY will only render on creation and with explicit calls to requestRender()
        renderMode = RENDERMODE_WHEN_DIRTY
    }
}