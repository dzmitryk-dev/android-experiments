package io.github.dzkoirn.androidexperiments.helloshaders

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.ln


class ShaderRenderer : GLSurfaceView.Renderer {
    private val TAG = "FractalRenderer"
//    private lateinit var mFractal: Fractal
    private lateinit var mShader: SimpliestShader

    private var mHeight = 0
    private var mWidth = 0

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
//        mFractal = Fractal()
        mShader = SimpliestShader()
    }

    override fun onDrawFrame(unused: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
//        val mMVPMatrix = floatArrayOf(
//            (-1.0 / mZoom).toFloat(), 0.0f, 0.0f, 0.0f,
//            0.0f, (1.0 / (mZoom * mRatio)).toFloat(), 0.0f, 0.0f,
//            0.0f, 0.0f, 1.0f, 0.0f, -mX.toFloat(), -mY.toFloat(), 0.0f, 1.0f
//        )
//        mFractal.draw(mMVPMatrix)
        mShader.draw()
    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        mWidth = width
        mHeight = height

        //Set viewport to fullscreen
        GLES20.glViewport(0, 0, width, height)
    }
}