package eu.metatools.reaktor.gdx.test

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable

class LayerDrawable(val drawables: List<Drawable>) : BaseDrawable(), Drawable {
    override fun draw(batch: Batch?, x: Float, y: Float, width: Float, height: Float) {
        for (drawable in drawables)
            drawable.draw(batch, x, y, width, height)
    }
}

class LayerTransformDrawable(val drawables: List<TransformDrawable>) : BaseDrawable(), TransformDrawable {
    override fun draw(
        batch: Batch?,
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
        for (drawable in drawables)
            drawable.draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
    }

    override fun draw(batch: Batch?, x: Float, y: Float, width: Float, height: Float) {
        for (drawable in drawables)
            drawable.draw(batch, x, y, width, height)
    }
}