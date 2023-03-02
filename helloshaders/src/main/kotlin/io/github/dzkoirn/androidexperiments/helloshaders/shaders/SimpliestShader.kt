package io.github.dzkoirn.androidexperiments.helloshaders.shaders

import android.opengl.GLES20
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SimpliestShader {

    private val vertexShaderCode =
        """attribute vec4 vPosition;
           void main() {
                gl_Position = vPosition;
           }
        """

    private val fragmentShaderCode =
        """ precision highp float;
            void main() {  
              gl_FragColor.rgb = vec3(1.0, 1.0, 0.0);
              gl_FragColor.a=1.0;
            }
        """

    private var mProgram = 0
    private var mPositionHandle = 0

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
    fun draw() {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")

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