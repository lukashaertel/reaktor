package eu.metatools.reaktor.gdx.shapes

/**
 * Defines if a shape is filled or if it's to be outlined with a line of given [lineWidth].
 */
interface ShapeMode {
    /**
     * True if filled.
     */
    val filled: Boolean

    /**
     * The width of the line, if not filled.
     */
    val lineWidth: Float
}

/**
 * [ShapeMode] set to outline with the given [lineWidth].
 * @param lineWidth The line width to use.
 */
class Line(override val lineWidth: Float) : ShapeMode {
    /**
     * Not filled.
     */
    override val filled = false

    /**
     * [ShapeMode] set to outline with line width of one.
     */
    companion object : ShapeMode {
        /**
         * Not filled.
         */
        override val filled = false

        /**
         * Line of width one.
         */
        override val lineWidth = 1f
    }
}

/**
 * [ShapeMode] set to fill.
 */
object Fill : ShapeMode {
    /**
     * Filled.
     */
    override val filled = true

    /**
     * No line width.
     */
    override val lineWidth = 0f
}