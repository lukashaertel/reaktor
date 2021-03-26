package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.Color


/**
 * Color field evaluation function.
 */
interface ColorField {
    companion object {
        /**
         * Creates a constant color with the given bits.
         */
        fun constant(color: Float) = object : ColorField {
            override fun invoke(ox: Float, oy: Float, w: Float, h: Float, x: Float, y: Float) =
                color
        }

        /**
         * Creates a constant color of [color]'s bits.
         */
        fun constant(color: Color) = object : ColorField {
            val bits = color.toFloatBits()
            override fun invoke(ox: Float, oy: Float, w: Float, h: Float, x: Float, y: Float) =
                bits
        }

        /**
         * Creates a function implementation.
         */
        inline operator fun invoke(crossinline block: (ox: Float, oy: Float, w: Float, h: Float, x: Float, y: Float) -> Float) =
            object : ColorField {
                override fun invoke(ox: Float, oy: Float, w: Float, h: Float, x: Float, y: Float) =
                    block(ox, oy, w, h, x, y)
            }
    }

    operator fun invoke(ox: Float, oy: Float, w: Float, h: Float, x: Float, y: Float): Float

    operator fun invoke(ox: Float, oy: Float, w: Float, h: Float) = { x: Float, y: Float ->
        invoke(ox, oy, w, h, x, y)
    }
}

/**
 * Creates a color field that is evaluated based on the screen coordinates.
 */
@Suppress("FunctionName")
inline fun ColorScreen(crossinline block: (screenX: Float, screenY: Float) -> Float) =
    ColorField { _, _, _, _, x, y -> block(x, y) }

/**
 * Creates a color field that is evaluated based on the absolute coordinates in the field.
 */
@Suppress("FunctionName")
inline fun ColorAbs(crossinline block: (x: Float, y: Float) -> Float) =
    ColorField { ox, oy, _, _, x, y -> block(x - ox, y - oy) }

/**
 * Creates a color field that is evaluated based on the u/v coordinates.
 */
@Suppress("FunctionName")
inline fun ColorUv(crossinline block: (u: Float, v: Float) -> Float) =
    ColorField { ox, oy, width, height, x, y -> block((x - ox) / width, (y - oy) / height) }

/**
 * A linear color function that is uniform.
 */
fun Color.solid() =
    ColorField.constant(this)

/**
 * Unit gradient in horizontal direction.
 */
fun horizontalGradient(from: Color, to: Color): ColorField {
    val capture = Color()
    return ColorUv { u, _ -> capture.set(from).lerp(to, u).toFloatBits() }
}

/**
 * Unit gradient in vertical direction.
 */
fun verticalGradient(from: Color, to: Color): ColorField {
    val capture = Color()
    return ColorUv { _, v ->
        capture.set(from).lerp(to, v).toFloatBits()
    }
}

/**
 * Color gradient of an absolute length. Must be used with an appropriate vertex distribution.
 */
fun absGradientLeft(from: Color, to: Color, length: Float): ColorField {
    val capture = Color()
    return ColorAbs { x, _ -> capture.set(from).lerp(to, (x / length).coerceIn(0f, 1f)).toFloatBits() }
}

/**
 * Color gradient of an absolute length. Must be used with an appropriate vertex distribution.
 */
fun absGradientBottom(from: Color, to: Color, length: Float): ColorField {
    val capture = Color()
    return ColorAbs { _, y -> capture.set(from).lerp(to, (y / length).coerceIn(0f, 1f)).toFloatBits() }
}

/**
 * Color gradient of an absolute length. Must be used with an appropriate vertex distribution.
 */
fun absGradientRight(from: Color, to: Color, length: Float): ColorField {
    val capture = Color()
    return ColorField { ox, _, w, _, x, _ ->
        capture.set(from).lerp(to, ((ox + w - x) / length).coerceIn(0f, 1f)).toFloatBits()
    }
}

/**
 * Color gradient of an absolute length. Must be used with an appropriate vertex distribution.
 */
fun absGradientTop(from: Color, to: Color, length: Float): ColorField {
    val capture = Color()
    return ColorField { _, oy, _, h, _, y ->
        capture.set(from).lerp(to, ((oy + h - y) / length).coerceIn(0f, 1f)).toFloatBits()
    }
}
