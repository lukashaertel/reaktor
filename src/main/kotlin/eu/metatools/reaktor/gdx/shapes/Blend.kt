package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.BufferUtils
import java.nio.IntBuffer

/**
 * Int buffer to get the blend values.
 */
val enableBlendForBuffer: IntBuffer = BufferUtils.newIntBuffer(16)

/**
 * Enables blending for the block. Resets the functions after.
 */
inline fun <T> enableBlendFor(block: () -> T): T {
    // Check if enabled.
    val enabled = Gdx.gl.glIsEnabled(GL20.GL_BLEND)

    // Get functions.
    val sf = enableBlendForBuffer.position(0).also { Gdx.gl.glGetIntegerv(GL20.GL_BLEND_SRC_ALPHA, it) }.get(0)
    val df = enableBlendForBuffer.position(0).also { Gdx.gl.glGetIntegerv(GL20.GL_BLEND_DST_ALPHA, it) }.get(0)

    try {
        // Enable and set functions.
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        // Return result of block.
        return block()
    } finally {
        // If was not enabled, disable now.
        if (!enabled)
            Gdx.gl.glDisable(GL20.GL_BLEND)

        // If values changed, reset them.
        if (sf != GL20.GL_SRC_ALPHA || df != GL20.GL_ONE_MINUS_SRC_ALPHA)
            Gdx.gl.glBlendFunc(sf, df)
    }

}