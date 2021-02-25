package eu.metatools.reaktor.gdx.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable
import com.badlogic.gdx.utils.BufferUtils
import java.nio.IntBuffer


val enableBlendForBuffer: IntBuffer = BufferUtils.newIntBuffer(16)

inline fun <T> enableBlendFor(block: () -> T): T {
    val enabled = Gdx.gl.glIsEnabled(GL20.GL_BLEND)
    val sf = enableBlendForBuffer.position(0).also { Gdx.gl.glGetIntegerv(GL20.GL_BLEND_SRC_ALPHA, it) }.get(0)
    val df = enableBlendForBuffer.position(0).also { Gdx.gl.glGetIntegerv(GL20.GL_BLEND_DST_ALPHA, it) }.get(0)

    try {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        return block()
    } finally {
        if (!enabled)
            Gdx.gl.glDisable(GL20.GL_BLEND)

        if (sf != GL20.GL_SRC_ALPHA || df != GL20.GL_ONE_MINUS_SRC_ALPHA)
            Gdx.gl.glBlendFunc(sf, df)
    }

}


abstract class ShapeDrawable : BaseDrawable(), TransformDrawable {
    companion object {
        val sharedShapeRenderer by lazy { ShapeRenderer() }
    }

    override fun draw(
        batch: Batch,
        x: Float,
        y: Float,
        originX: Float,
        originY: Float,
        width: Float,
        height: Float,
        scaleX: Float,
        scaleY: Float,
        rotation: Float,
    ) {
        // Pause batch.
        val resume = batch.isDrawing
        if (resume) batch.end()

        // Apply matrix for transform.
        sharedShapeRenderer.projectionMatrix = batch.projectionMatrix
        sharedShapeRenderer.transformMatrix =
            batch.transformMatrix.cpy().scale(scaleX, scaleY, 1f).rotate(0f, 0f, 1f, rotation)

        // Draw.
        enableBlendFor { draw(sharedShapeRenderer, batch.color, x, y, width, height) }

        // Resume batch.
        if (resume) batch.begin()
    }

    override fun draw(batch: Batch, x: Float, y: Float, width: Float, height: Float) {
        // Pause batch.
        val resume = batch.isDrawing
        if (resume) batch.end()

        // Apply matrix.
        sharedShapeRenderer.projectionMatrix = batch.projectionMatrix
        sharedShapeRenderer.transformMatrix = batch.transformMatrix

        // Draw.
        enableBlendFor { draw(sharedShapeRenderer, batch.color, x, y, width, height) }

        // Resume batch.
        if (resume) batch.begin()
    }

    protected abstract fun draw(
        shapeRenderer: ShapeRenderer,
        tint: Color,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
    )
}

data class RectDrawable(val type: ShapeRenderer.ShapeType, val color: Color) : ShapeDrawable() {
    override fun draw(shapeRenderer: ShapeRenderer, tint: Color, x: Float, y: Float, width: Float, height: Float) {
        // TODO: Fucking hell am I supposed to include those while drawing.
        if (width < leftWidth + rightWidth) return
        if (height < topHeight + bottomHeight) return

        val appliedColor = if (tint == Color.WHITE) color else color.cpy().mul(tint)

        shapeRenderer.begin(type)
        shapeRenderer.rect(
            leftWidth + x,
            topHeight + y,
            width - (leftWidth + rightWidth),
            height - (topHeight + bottomHeight),
            appliedColor, appliedColor, appliedColor, appliedColor)

        shapeRenderer.end()
    }
}

data class Gradient(val type: ShapeRenderer.ShapeType, val colors: List<Color>) : ShapeDrawable() {
    companion object {
        fun horizontal(type: ShapeRenderer.ShapeType, colorLeft: Color, colorRight: Color) =
            Gradient(type, listOf(colorLeft, colorRight, colorRight, colorLeft))

        fun vertical(type: ShapeRenderer.ShapeType, colorTop: Color, colorBottom: Color) =
            Gradient(type, listOf(colorTop, colorTop, colorBottom, colorBottom))

        fun diagonalDown(type: ShapeRenderer.ShapeType, colorTopLeft: Color, colorBottomRight: Color): Gradient {
            val intermediate = colorTopLeft.cpy().lerp(colorBottomRight, 0.5f)
            return Gradient(type, listOf(colorTopLeft, intermediate, colorBottomRight, intermediate))
        }

        fun diagonalUp(type: ShapeRenderer.ShapeType, colorTopRight: Color, colorBottomLeft: Color): Gradient {
            val intermediate = colorTopRight.cpy().lerp(colorBottomLeft, 0.5f)
            return Gradient(type, listOf(intermediate, colorTopRight, intermediate, colorBottomLeft))
        }
    }

    override fun draw(shapeRenderer: ShapeRenderer, tint: Color, x: Float, y: Float, width: Float, height: Float) {
        if (width < leftWidth + rightWidth) return
        if (height < topHeight + bottomHeight) return

        val (col1, col2, col3, col4) = colors.map {
            if (tint == Color.WHITE) it else it.cpy().mul(tint)
        }


        shapeRenderer.begin(type)
        shapeRenderer.rect(
            leftWidth + x,
            topHeight + y,
            width - (leftWidth + rightWidth),
            height - (topHeight + bottomHeight),
            col1, col2, col3, col4)

        shapeRenderer.end()
    }
}