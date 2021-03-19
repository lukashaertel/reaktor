package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable


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
        enableBlendFor { draw(sharedShapeRenderer,  x, y, width, height) }

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
        enableBlendFor { draw(sharedShapeRenderer, x, y, width, height) }

        // Resume batch.
        if (resume) batch.begin()
    }

    protected abstract fun draw(
        shapeRenderer: ShapeRenderer,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
    )
}

