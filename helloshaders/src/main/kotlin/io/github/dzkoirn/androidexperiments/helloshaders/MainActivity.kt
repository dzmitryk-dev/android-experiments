package io.github.dzkoirn.androidexperiments.helloshaders

import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mGLView: FractalSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLView =  FractalSurfaceView(this)
        setContentView(mGLView)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //Change to immersive mode. This is only available in kitkat and later, so check the os version
        if (hasFocus) {
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }


    override fun onResume() {
        super.onResume()
        mGLView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGLView.onPause()
    }
}