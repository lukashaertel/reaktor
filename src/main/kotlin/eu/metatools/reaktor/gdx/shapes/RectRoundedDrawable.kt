package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

inline class Corners(val value: Byte) {
    companion object {
        val bottomLeft = Corners(1)
        val topLeft = Corners(2)
        val topRight = Corners(4)
        val bottomRight = Corners(8)

        val none = Corners(0)
        val all = Corners(15)
    }

    operator fun contains(i: Int): Boolean = when (i) {
        0 -> (value and bottomLeft.value) > 0
        1 -> (value and topLeft.value) > 0
        2 -> (value and topRight.value) > 0
        3 -> (value and bottomRight.value) > 0
        else -> false
    }

    infix fun and(other: Corners): Corners =
        Corners(value or other.value)

    infix fun except(other: Corners): Corners =
        Corners(value and other.value.inv())
}

/**
 * Drawable defining a rectangle that is colored according to [colorAt] and filled or outlined according to [mode]. The
 * corners are rounded with a radius of [r]. The corners consist of [resolution] segments, defaulting to eight.
 */
data class RectRoundedDrawable(
    val mode: ShapeMode,
    val r: Float,
    val colorAt: ColorAt,
    val resolution: Int = 8,
    val corners: Corners = Corners.all,
) : ShapeDrawable() {
    /**
     * Creates a rounded rectangle drawable with a solid color.
     */
    constructor(mode: ShapeMode, r: Float, color: Color, resolution: Int = 8, corners: Corners = Corners.all)
            : this(mode, r, color.solid(), resolution, corners)

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
        fun colorFnAt(ax: Float, ay: Float) =
            colorAt(ax, ay, ax - x, ay - y, (ax - x) / width, (ay - y) / height)

        // Empty, return.
        if (width <= 0f) return
        if (height <= 0f) return

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
                        val nc = colorFnAt(nx, ny)
                        painter.next(nx, ny, nc)
                    }
                else
                    painter.next(ox, oy, colorFnAt(ox, oy))
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
                            val nc = colorFnAt(nx, ny)
                            painter.next(nx, ny, nc)
                        }
                    else
                        painter.next(ox, oy, colorFnAt(ox, oy))
                }

                // Close shape and end renderer.
                painter.close()
                shapeRenderer.end()
            }
        }
    }
}