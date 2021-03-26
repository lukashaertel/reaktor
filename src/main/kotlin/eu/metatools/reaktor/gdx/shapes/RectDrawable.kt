package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * Drawable defining a rectangle that is colored according to [color] and filled or outlined according to [mode].
 */
data class RectDrawable(val mode: ShapeMode, val color: ColorUV) : ShapeDrawable() {
    /**
     * Creates a rectangle drawable with a solid color.
     */
    constructor(mode: ShapeMode, color: Color) : this(mode, color.solid())

    override fun draw(shapeRenderer: ShapeRenderer, x: Float, y: Float, width: Float, height: Float) {
        if (width <= 0f) return
        if (height <= 0f) return

        val colorAbs = color.toAbs(x, y, width, height)

        if (mode.filled) {
            // Filled, begin shape filled and start using fill painter.
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            val painter = PainterFill(shapeRenderer.renderer)

            // Add all corners directly.
            painter.next(x, y, colorAbs)
            painter.next(x, y + height, colorAbs)
            painter.next(x + width, y + height, colorAbs)
            painter.next(x + width, y, colorAbs)

            // Close shape and end renderer.
            painter.close()
            shapeRenderer.end()
        } else {
            // Line, assign given line width.
            lineWidthFor(mode.lineWidth) {
                // Filed, begin shape filled and start using line painter.
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                val painter = PainterLine(shapeRenderer.renderer)

                // Add all corners directly.
                painter.next(x, y, colorAbs)
                painter.next(x, y + height, colorAbs)
                painter.next(x + width, y + height, colorAbs)
                painter.next(x + width, y, colorAbs)

                // Close shape and end renderer.
                painter.close()
                shapeRenderer.end()
            }
        }
    }
}