package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils

/**
 * Evaluates a linear color field. Returns the float bits.
 */
typealias ColorAt = (x: Float, y: Float, ox: Float, oy: Float, ux: Float, uy: Float) -> Float

/**
 * A linear color function that is uniform.
 */
fun Color.solid(): ColorAt {
    val bits = toFloatBits()
    return { _, _, _, _, _, _ -> bits }
}

/**
 * Unit gradient in horizontal direction.
 */
fun horizontalGradient(from: Color, to: Color): ColorAt {
    val capture = Color()
    return { _, _, _, _, ux, _ ->
        capture.set(from).lerp(to, ux).toFloatBits()
    }
}

/**
 * Unit gradient in vertical direction.
 */
fun verticalGradient(from: Color, to: Color): ColorAt {
    val capture = Color()
    return { _, _, _, _, _, uy ->
        capture.set(from).lerp(to, uy).toFloatBits()
    }
}