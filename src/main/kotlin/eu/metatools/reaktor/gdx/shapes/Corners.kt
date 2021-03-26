package eu.metatools.reaktor.gdx.shapes

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

/**
 * Corner flags.
 * @property value The bytes representing the corners.
 */
inline class Corners(val value: Byte) {
    companion object {
        /**
         * Bottom-left corner.
         */
        val bottomLeft = Corners(1)

        /**
         * Top-left corner.
         */
        val topLeft = Corners(2)

        /**
         * Top-right corner.
         */
        val topRight = Corners(4)

        /**
         * Bottom-right corner.
         */
        val bottomRight = Corners(8)

        /**
         * No corners.
         */
        val none = Corners(0)

        // All corners.
        val all = Corners(15)

        /**
         * Both left corners/
         */
        val left = bottomLeft and topLeft

        /**
         * Both right corners/
         */
        val right = bottomRight and topRight

        /**
         * Both top corners.
         */
        val top = topLeft and topRight

        /**
         * Both bottom corners.
         */
        val bottom = bottomLeft and bottomRight
    }

    /**
     * True if the clockwise corner index from bottom-left is included in the receiver.
     */
    operator fun contains(i: Int): Boolean = when (i) {
        0 -> (value and bottomLeft.value) > 0
        1 -> (value and topLeft.value) > 0
        2 -> (value and topRight.value) > 0
        3 -> (value and bottomRight.value) > 0
        else -> false
    }

    /**
     * Adds the [other] corner to the receiver for a new corner flag object.
     */
    infix fun and(other: Corners): Corners =
        Corners(value or other.value)

    /**
     * Removes the [other] corner from the receiver for a new corner flag object.
     */
    infix fun except(other: Corners): Corners =
        Corners(value and other.value.inv())
}