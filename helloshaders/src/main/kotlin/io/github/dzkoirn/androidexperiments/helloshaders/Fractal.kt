package io.github.dzkoirn.androidexperiments.helloshaders

import android.opengl.GLES20
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Fractal {

    private val vertexShaderCode =
        """attribute vec4 vPosition;
           void main() {
                gl_Position = vPosition;
           }
        """

    private val fragmentShaderCode =
        """ precision highp float;
            uniform mat4 uMVPMatrix;
            void main() {  
            // Transform given position to coordinate space
              vec2 p = (uMVPMatrix * vec4(gl_PointCoord,0,1)).xy;
              vec2 c = p;  // Set default color to black in HSV
              vec3 color=vec3(0.0,0.0,0.0); // Use 200 as an arbitrary limit. The higher the number, the slower and more detailed it will be
              for(int i=0;i<200;i++) {  // Perform z = z^2 + c using p, which represents the real and imaginary parts of z
              	  p= vec2(p.x*p.x-p.y*p.y,2.0*p.x*p.y)+c;
                  if (dot(p,p)>4.0){  // colorRegulator continuously increases smoothly by 1 for every additional step it takes to break
                     float colorRegulator = float(i-1)-log(log(length(p)))/log(2.0);  // Set color to a cycling color scheme using the smooth number
                     color = vec3(0.95 + .012*colorRegulator , 1.0, .2+.4*(1.0+sin(.3*colorRegulator)));
                     break;
                  }
              }
              //Convert HSV to RGB. Algorithm from https://gist.github.com/patriciogonzalezvivo/114c1653de9e3da6e1e3
              vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
              vec3 m = abs(fract(color.xxx + K.xyz) * 6.0 - K.www);
              gl_FragColor.rgb = color.z * mix(K.xxx, clamp(m - K.xxx, 0.0, 1.0), color.y);
              gl_FragColor.a=1.0;
            }
        """

//    private val fragmentShaderCode =
//        """ precision highp float;
//            uniform mat4 uMVPMatrix;
//            void main() {  // Transform given position to coordinate space
//              gl_FragColor.rgb = vec3(0.0, 1.0, 0.0);
//              gl_FragColor.a=1.0;
//            }
//        """

    private var mProgram = 0
    private var mPositionHandle = 0
    private var mMVPMatrixHandle = 0

    private val squareCoords = floatArrayOf(
        -1.0f, 1.0f, 0.0f,  // top left
        -1.0f, -1.0f, 0.0f,  // bottom left
        1.0f, -1.0f, 0.0f,  // bottom right
        1.0f, 1.0f, 0.0f
    ) // top right

    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices

    // initialize vertex byte buffer for shape coordinates
    private val vertexBuffer = ByteBuffer.allocateDirect(
        squareCoords.size * 4 // (# of coordinate values * 4 bytes per float)
    ).apply {
        order(ByteOrder.nativeOrder())
    }.asFloatBuffer().apply {
        put(squareCoords)
        position(0)
    }

    // initialize byte buffer for the draw list
    private val drawListBuffer = ByteBuffer.allocateDirect( // (# of coordinate values * 2 bytes per short)
        drawOrder.size * 2
    ).apply {
        order(ByteOrder.nativeOrder())
    }.asShortBuffer().apply {
        put(drawOrder)
        position(0)
    }

    init {
        // prepare shaders and OpenGL program
        val vertexShader: Int = loadShader(
            GLES20.GL_VERTEX_SHADER,
            vertexShaderCode
        )
        val fragmentShader: Int = loadShader(
            GLES20.GL_FRAGMENT_SHADER,
            fragmentShaderCode
        )
        mProgram = GLES20.glCreateProgram() // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader) // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader) // add the fragment shader to program
        GLES20.glLinkProgram(mProgram) // create OpenGL program executables
    }

    /**
     * Utility method from Android Tutorials for compiling a OpenGL shader.
     *
     *
     * **Note:** When developing shaders, use the checkGlError()
     * method to debug shader coding errors.
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    private fun loadShader(type: Int, shaderCode: String) =
        GLES20.glCreateShader(type).also { shader ->
            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    fun draw(mvpMatrix: FloatArray?) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        GLES20.glVertexAttribPointer(
            mPositionHandle, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            vertexStride,
            vertexBuffer
        )

        // Draw the square
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT, drawListBuffer
        )

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle)
        checkGlError("Test");
    }

    /**
     * Utility method from Android Tutorials for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    private fun checkGlError(glOperation: String) {
        var error: Int
        while (GLES20.glGetError().also { error = it } != GLES20.GL_NO_ERROR) {
            Log.e(TAG, "$glOperation: glError $error")
            throw RuntimeException("$glOperation: glError $error")
        }
    }

    private companion object Constants {
        // number of coordinates per vertex in this array
        const val COORDS_PER_VERTEX = 3
        const val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex
        const val TAG = "Fractal"
    }
}