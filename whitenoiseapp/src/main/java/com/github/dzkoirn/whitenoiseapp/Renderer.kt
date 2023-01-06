package com.github.dzkoirn.whitenoiseapp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.SurfaceHolder
import kotlinx.coroutines.*
import kotlin.random.Random

@OptIn(DelicateCoroutinesApi::class)
class Renderer(
    private val coroutineScope: CoroutineScope
) : SurfaceHolder.Callback {

    private lateinit var renderJob: Job
    private lateinit var drawJob: Job

    @Volatile
    private var holder: SurfaceHolder? = null
        @Synchronized get
        @Synchronized set
    private val holderPaint = Paint()
    private val textPaint = Paint().apply {
        color = Color.YELLOW
        textSize = 64.0f
    }

    @Volatile
    private var bitmap: Bitmap? = null
        @Synchronized get
        @Synchronized set
    @Volatile
    private var bitmapCanvas: Canvas? = null
        @Synchronized get
        @Synchronized set

    private var height: Int = 0
    private var width: Int = 0

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("Render", "surfaceCreated")
        this.holder = holder.apply {
            setKeepScreenOn(true)
        }
        renderJob = coroutineScope.launch(newSingleThreadContext("renderThread")) {
            render(this)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("Render", "surfaceChanged format = $format, width = $width, height = $height")
        this.holder = holder
        this.width = width
        this.height = height
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmapCanvas = bitmap?.let { Canvas(it) }
        drawJob = coroutineScope.launch(newFixedThreadPoolContext(nThreads = 4, name= "drawerThread")) {
           launch { draw(this) }
           launch { draw(this) }
           launch { draw(this) }
           launch { draw(this) }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("Render", "surfaceDestroyed")
        renderJob.cancel()
        drawJob.cancel()
        this.holder = null
        bitmapCanvas = null
        bitmap = null
    }

    private suspend fun render(coroutineScope: CoroutineScope) {
        var iteration: Long = 0
        while (coroutineScope.isActive) {
            holder?.let { surfaceHolder ->
                surfaceHolder.surface?.let { surface ->
                    if (surface.isValid) {
                        surfaceHolder.lockCanvas()?.let { canvas ->
                            bitmap?.let { bitmap ->
                                canvas.drawBitmap(bitmap, 0.0f, 0.0f, holderPaint)
                                canvas.drawText("Iteration: ${iteration++}", 64.0f, 64.0f, textPaint)
                            }
                            surfaceHolder.unlockCanvasAndPost(canvas)
                        }
                    }
                }
            }
            delay(100)
        }
    }

    private fun draw(coroutineScope: CoroutineScope) {
        val random = Random(System.currentTimeMillis())
        val bitmapPaint = Paint()
        while (coroutineScope.isActive) {
            val randomHeight = random.nextInt(height)
            val randomWidth = random.nextInt(width)
            val randomColor = random.nextInt(from = 0, until = 0xFFFFFF) or 0xFF000000.toInt()

            bitmapPaint.color = randomColor
            bitmapCanvas?.drawPoint(randomWidth.toFloat(), randomHeight.toFloat(), bitmapPaint)
        }
    }
}