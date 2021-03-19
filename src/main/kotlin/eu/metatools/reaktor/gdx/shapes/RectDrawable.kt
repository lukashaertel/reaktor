package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

data class RectDrawable(val filled: Boolean, val colorAt: ColorAt) : ShapeDrawable() {
    constructor(filled: Boolean, color: Color) : this(filled, color.solid())

    override fun draw(shapeRenderer: ShapeRenderer, x: Float, y: Float, width: Float, height: Float) {
        if (width <= 0f) return
        if (height <= 0f) return

        val renderer = shapeRenderer.renderer

        if (filled) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            renderer.color(colorAt(x, y, 0f, 0f, 0f, 0f))
            renderer.vertex(x, y, 0f)
            renderer.color(colorAt(x + width, y, width, 0f, 1f, 0f))
            renderer.vertex(x + width, y, 0f)
            renderer.color(colorAt(x + width, y + height, width, height, 1f, 1f))
            renderer.vertex(x + width, y + height, 0f)
            renderer.color(colorAt(x + width, y + height, width, height, 1f, 1f))
            renderer.vertex(x + width, y + height, 0f)
            renderer.color(colorAt(x, y + height, 0f, height, 0f, 1f))
            renderer.vertex(x, y + height, 0f)
            renderer.color(colorAt(x, y, 0f, 0f, 0f, 0f))
            renderer.vertex(x, y, 0f)
            shapeRenderer.end()
        } else {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
            renderer.color(colorAt(x, y, 0f, 0f, 0f, 0f))
            renderer.vertex(x, y, 0f)
            renderer.color(colorAt(x + width, y, width, 0f, 1f, 0f))
            renderer.vertex(x + width, y, 0f)
            renderer.color(colorAt(x + width, y, width, 0f, 1f, 0f))
            renderer.vertex(x + width, y, 0f)
            renderer.color(colorAt(x + width, y + height, width, height, 1f, 1f))
            renderer.vertex(x + width, y + height, 0f)
            renderer.color(colorAt(x + width, y + height, width, height, 1f, 1f))
            renderer.vertex(x + width, y + height, 0f)
            renderer.color(colorAt(x, y + height, 0f, height, 0f, 1f))
            renderer.vertex(x, y + height, 0f)
            renderer.color(colorAt(x, y + height, 0f, height, 0f, 1f))
            renderer.vertex(x, y + height, 0f)
            renderer.color(colorAt(x, y, 0f, 0f, 0f, 0f))
            renderer.vertex(x, y, 0f)
            shapeRenderer.end()
        }
    }
}