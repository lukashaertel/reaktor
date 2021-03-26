package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.BufferUtils
import java.nio.FloatBuffer

/**
 * Float buffer to get the line width.
 */
val lineWidthForBuffer: FloatBuffer = BufferUtils.newFloatBuffer(16)

/**
 * Sets the line width for the block.
 */
inline fun <T> lineWidthFor(width: Float, block: () -> T): T {
    // Get original line width.
    val lw = lineWidthForBuffer.position(0).also { Gdx.gl.glGetFloatv(GL20.GL_LINE_WIDTH, it) }.get(0)

    try {
        // Set to new line width.
        Gdx.gl.glLineWidth(width)

        // Return result of block.
        return block()
    } finally {
        // Reset.
        Gdx.gl.glLineWidth(lw)

    }

}