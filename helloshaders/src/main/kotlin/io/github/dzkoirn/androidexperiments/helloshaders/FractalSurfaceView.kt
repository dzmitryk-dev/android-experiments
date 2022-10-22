package io.github.dzkoirn.androidexperiments.helloshaders

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener


class FractalSurfaceView(context: Context) : GLSurfaceView(context) {
    private val mRenderer: FractalRenderer
    private val mDetector: ScaleGestureDetector

    // Position represents focus while twoFingers is true and previous position otherwise
    var mPreviousX = 0f
    var mPreviousY = 0f
    var lastNumFingers = 0

    init {
        setEGLContextClientVersion(2)
        mRenderer = FractalRenderer()
        mDetector = ScaleGestureDetector(getContext(), ScaleListener())
        setRenderer(mRenderer)

        //RENDERMODE_WHEN_DIRTY will only render on creation and with explicit calls to requestRender()
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        mDetector.onTouchEvent(e)
        if (e.actionIndex > 1) {
            return true
        }
        var numFingers = e.pointerCount
        when (e.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                mPreviousX = 0.0f
                mPreviousY = 0.0f

                //Get the average of the fingers on the screen as the current position
                var i = 0
                while (i < numFingers) {
                    mPreviousX += e.getX(i)
                    mPreviousY += e.getY(i)
                    i++
                }
                mPreviousX /= numFingers.toFloat()
                mPreviousY /= numFingers.toFloat()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mPreviousX = 0.0f
                mPreviousY = 0.0f

                //Get the average of the remaining fingers on the screen as the current position
                var i = 0
                while (i < numFingers) {
                    if (i == e.actionIndex) {
                        i++
                        continue
                    }
                    mPreviousX += e.getX(i)
                    mPreviousY += e.getY(i)
                    i++
                }
                numFingers -= 1
                mPreviousX /= numFingers.toFloat()
                mPreviousY /= numFingers.toFloat()
            }
            MotionEvent.ACTION_MOVE -> {
                var tempX = 0.0f
                var tempY = 0.0f

                //Get the average of the fingers on the screen as the current position
                var i = 0
                while (i < numFingers) {
                    tempX += e.getX(i)
                    tempY += e.getY(i)
                    i++
                }
                tempX /= numFingers.toFloat()
                tempY /= numFingers.toFloat()
                if (lastNumFingers == numFingers) {
                    //Sometimes a third finger doesn't register under point, so track it separately
                    mRenderer.add((tempX - mPreviousX).toDouble(), (tempY - mPreviousY).toDouble())
                }
                mPreviousX = tempX
                mPreviousY = tempY
                requestRender()
            }
        }
        lastNumFingers = numFingers
        return true
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mRenderer.zoom(
                detector.scaleFactor.toDouble(),
                detector.focusX.toDouble(),
                detector.focusY.toDouble()
            )
            return true
        }
    }
}