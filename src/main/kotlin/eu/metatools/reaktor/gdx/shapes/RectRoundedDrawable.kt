package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils

data class RectRoundedDrawable(
    val filled: Boolean, val r: Float, val colorAt: ColorAt, val resolution: Int = 8,
) : ShapeDrawable() {
    constructor(filled: Boolean, r: Float, color: Color, resolution: Int = 8)
            : this(filled, r, color.solid(), resolution)

    private inline fun withCenters(
        d: Float,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        block: (i: Int, cx: Float, cy: Float) -> Unit,
    ) {
        block(0, x + d, y + d)
        block(1, x + d, y + height - d)
        block(2, x + width - d, y + height - d)
        block(3, x + width - d, y + d)
    }

    override fun draw(shapeRenderer: ShapeRenderer, x: Float, y: Float, width: Float, height: Float) {
        fun colorFnAt(ax: Float, ay: Float) =
            colorAt(ax, ay, ax - x, ay - y, (ax - x) / width, (ay - y) / height)

        if (width <= 0f) return
        if (height <= 0f) return

        val renderer = shapeRenderer.renderer

        val d = minOf(r, minOf(height, width) / 2f)

        val sx = x + width - d
        val sy = y
        val sc = colorFnAt(sx, sy)

        if (filled) {
            val ox = x + width / 2f
            val oy = y + height / 2f
            val oc = colorFnAt(ox, oy)

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

            var lx = sx
            var ly = sy
            var lc = sc

            withCenters(d, x, y, width, height) { i, cx, cy ->
                repeat(resolution.inc()) {
                    val angle = -90f - i * 90f - (it * 90f / resolution)
                    val nx = cx + d * MathUtils.cosDeg(angle)
                    val ny = cy + d * MathUtils.sinDeg(angle)
                    val nc = colorFnAt(nx, ny)

                    renderer.color(oc)
                    renderer.vertex(ox, oy, 0f)

                    renderer.color(lc)
                    renderer.vertex(lx, ly, 0f)

                    renderer.color(nc)
                    renderer.vertex(nx, ny, 0f)

                    lx = nx
                    ly = ny
                    lc = nc
                }
            }

            shapeRenderer.end()
        } else {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
            var lx = sx
            var ly = sy
            var lc = sc

            withCenters(d, x, y, width, height) { i, cx, cy ->
                repeat(resolution.inc()) {
                    val angle = -90f - i * 90f - (it * 90f / resolution)
                    val nx = cx + d * MathUtils.cosDeg(angle)
                    val ny = cy + d * MathUtils.sinDeg(angle)
                    val nc = colorFnAt(nx, ny)

                    renderer.color(lc)
                    renderer.vertex(lx, ly, 0f)

                    renderer.color(nc)
                    renderer.vertex(nx, ny, 0f)

                    lx = nx
                    ly = ny
                    lc = nc
                }
            }

            shapeRenderer.end()
        }
    }
}