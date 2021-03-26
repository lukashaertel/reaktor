package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer

/**
 * Consecutive painter handle. Adds points to a shape and provides methods to close it based on internal state.
 */
interface Painter {
    /**
     * Appends a point.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param color The color of the point.
     */
    fun next(x: Float, y: Float, color: Float)

    /**
     * Closes the shape. Allows reuse of the Painter.
     */
    fun close()
}

/**
 * Invoke [Painter.next] with the color evaluated at the position. A [ColorUV] might need to be converted
 * with [ColorUV.toAbs].
 */
fun Painter.next(x: Float, y: Float, color: ColorAbs) =
    next(x, y, color(x, y))

/**
 * Paints a line in [Painter] mode.
 * @param immediateModeRenderer The target renderer.
 */
class PainterLine(
    private val immediateModeRenderer: ImmediateModeRenderer,
) : Painter {
    // First coordinate or NaN if not assigned.
    private var firstX = Float.NaN
    private var firstY = Float.NaN
    private var firstColor = Float.NaN

    // Last coordinate used.
    private var lastX = Float.NaN
    private var lastY = Float.NaN
    private var lastColor = Float.NaN

    override fun next(x: Float, y: Float, color: Float) {
        if (firstX.isNaN()) {
            firstX = x
            firstY = y
            firstColor = color
        }

        immediateModeRenderer.color(lastColor)
        immediateModeRenderer.vertex(lastX, lastY, 0f)

        immediateModeRenderer.color(color)
        immediateModeRenderer.vertex(x, y, 0f)

        lastX = x
        lastY = y
        lastColor = color
    }

    override fun close() {
        immediateModeRenderer.color(lastColor)
        immediateModeRenderer.vertex(lastX, lastY, 0f)

        immediateModeRenderer.color(firstColor)
        immediateModeRenderer.vertex(firstX, firstY, 0f)

        firstX = Float.NaN
        firstY = Float.NaN
        firstColor = Float.NaN
        lastX = Float.NaN
        lastY = Float.NaN
        lastColor = Float.NaN
    }
}

/**
 * Paints a filled convex shape in [Painter] mode.
 * @param immediateModeRenderer The target renderer.
 */
class PainterFill(
    private val immediateModeRenderer: ImmediateModeRenderer,
) : Painter {
    // First coordinate or NaN if not assigned.
    private var firstX = Float.NaN
    private var firstY = Float.NaN
    private var firstColor = Float.NaN

    // Last coordinate used.
    private var lastX = Float.NaN
    private var lastY = Float.NaN
    private var lastColor = Float.NaN

    override fun next(x: Float, y: Float, color: Float) {
        if (firstX.isNaN()) {
            firstX = x
            firstY = y
            firstColor = color

            lastX = x
            lastY = y
            lastColor = color
            return
        }

        immediateModeRenderer.color(firstColor)
        immediateModeRenderer.vertex(firstX, firstY, 0f)

        immediateModeRenderer.color(lastColor)
        immediateModeRenderer.vertex(lastX, lastY, 0f)

        immediateModeRenderer.color(color)
        immediateModeRenderer.vertex(x, y, 0f)

        lastX = x
        lastY = y
        lastColor = color
    }

    override fun close() {
        firstX = Float.NaN
        firstY = Float.NaN
        firstColor = Float.NaN
        lastX = Float.NaN
        lastY = Float.NaN
        lastColor = Float.NaN
    }
}
