package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

// TODO: Verify this. Not very useful ATM.
abstract class PainterDrawable(val mode: ShapeMode) : ShapeDrawable() {

    abstract fun draw(painter: Painter, x: Float, y: Float, width: Float, height: Float)

    override fun draw(shapeRenderer: ShapeRenderer, x: Float, y: Float, width: Float, height: Float) {
        if (width <= 0f) return
        if (height <= 0f) return

        if (mode.filled) {
            // Render filled shape with painter.
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            draw(PainterFill(shapeRenderer.renderer), x, y, width, height)
            shapeRenderer.end()
        } else {
            // Line, assign given line width.
            lineWidthFor(mode.lineWidth) {
                // Render line with painter.
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                draw(PainterLine(shapeRenderer.renderer), x, y, width, height)
                shapeRenderer.end()
            }
        }
    }
}