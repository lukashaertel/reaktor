package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * Drawable defining a rectangle that is colored according to [colorAt] and filled or outlined according to [mode].
 */
data class RectDrawable(val mode: ShapeMode, val colorAt: ColorAt) : ShapeDrawable() {
    /**
     * Creates a rectangle drawable with a solid color.
     */
    constructor(mode: ShapeMode, color: Color) : this(mode, color.solid())

    override fun draw(shapeRenderer: ShapeRenderer, x: Float, y: Float, width: Float, height: Float) {
        if (width <= 0f) return
        if (height <= 0f) return

        val renderer = shapeRenderer.renderer

        if (mode.filled) {

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            val painter = PainterFill(shapeRenderer.renderer)
            painter.next(x, y, colorAt(x, y, 0f, 0f, 0f, 0f))
            painter.next(x, y + height, colorAt(x, y + height, 0f, height, 0f, 1f))
            painter.next(x + width, y + height, colorAt(x + width, y + height, width, height, 1f, 1f))
            painter.next(x + width, y, colorAt(x + width, y, width, 0f, 1f, 0f))
            painter.close()
            shapeRenderer.end()
        } else {
            lineWidthFor(mode.lineWidth) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                val painter = PainterLine(shapeRenderer.renderer)
                painter.next(x, y, colorAt(x, y, 0f, 0f, 0f, 0f))
                painter.next(x, y + height, colorAt(x, y + height, 0f, height, 0f, 1f))
                painter.next(x + width, y + height, colorAt(x + width, y + height, width, height, 1f, 1f))
                painter.next(x + width, y, colorAt(x + width, y, width, 0f, 1f, 0f))
                painter.close()
                shapeRenderer.end()
            }
        }
    }
}