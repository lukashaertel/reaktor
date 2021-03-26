package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils

/**
 * Drawable defining a rectangle that is colored according to [color] and filled or outlined according to [mode]. The
 * corners are rounded with a radius of [r]. The corners consist of [resolution] segments, defaulting to eight.
 */
data class RectRoundedDrawable(
    val mode: ShapeMode,
    val r: Float,
    val color: ColorUV,
    val corners: Corners = Corners.all,
    val resolution: Int = 8,
) : ShapeDrawable() {
    /**
     * Creates a rounded rectangle drawable with a solid color.
     */
    constructor(mode: ShapeMode, r: Float, color: Color, corners: Corners = Corners.all, resolution: Int = 8)
            : this(mode, r, color.solid(), corners, resolution)

    /**
     * Runs the given [block] with the corner parameters. The block will have the number of the corner passed,
     * then the coordinates of the outer edge, then the inset edge.
     * @param d The radius to inset.
     * @param x The starting x-coordinate position.
     * @param y The starting y-coordinate position.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     * @param block The block to run with the corner coordinates.
     */
    private inline fun withCenters(
        d: Float,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        block: (i: Int, ox: Float, oy: Float, ix: Float, iy: Float) -> Unit,
    ) {
        block(0, x, y, x + d, y + d)
        block(1, x, y + height, x + d, y + height - d)
        block(2, x + width, y + height, x + width - d, y + height - d)
        block(3, x + width, y, x + width - d, y + d)
    }

    override fun draw(shapeRenderer: ShapeRenderer, x: Float, y: Float, width: Float, height: Float) {
        // Empty, return.
        if (width <= 0f) return
        if (height <= 0f) return

        val colorAbs = color.toAbs(x, y, width, height)

        // Determine radius limit.
        val d = minOf(r, minOf(height, width) / 2f)

        // Decide on draw mode.
        if (mode.filled) {
            // Filled, begin shape filled and start using fill painter.
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            val painter = PainterFill(shapeRenderer.renderer)

            // Run with all corners.
            withCenters(d, x, y, width, height) { i, ox, oy, cx, cy ->
                if (i in corners)
                    repeat(resolution.inc()) {
                        val angle = -90f - i * 90f - (it * 90f / resolution)
                        val nx = cx + d * MathUtils.cosDeg(angle)
                        val ny = cy + d * MathUtils.sinDeg(angle)
                        painter.next(nx, ny, colorAbs)
                    }
                else
                    painter.next(ox, oy, colorAbs)
            }

            // Close shape and end renderer.
            painter.close()
            shapeRenderer.end()
        } else {
            // Line, assign given line width.
            lineWidthFor(mode.lineWidth) {
                // Filed, begin shape filled and start using line painter.
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                val painter = PainterLine(shapeRenderer.renderer)

                // Run with all corners.
                withCenters(d, x, y, width, height) { i, ox, oy, cx, cy ->
                    if (i in corners)
                        repeat(resolution.inc()) {
                            val angle = -90f - i * 90f - (it * 90f / resolution)
                            val nx = cx + d * MathUtils.cosDeg(angle)
                            val ny = cy + d * MathUtils.sinDeg(angle)
                            painter.next(nx, ny, colorAbs)
                        }
                    else
                        painter.next(ox, oy, colorAbs)
                }

                // Close shape and end renderer.
                painter.close()
                shapeRenderer.end()
            }
        }
    }
}