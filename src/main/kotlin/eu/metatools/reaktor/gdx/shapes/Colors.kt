package eu.metatools.reaktor.gdx.shapes

import com.badlogic.gdx.graphics.Color

/**
 * A color function evaluated at u/v coordinates.
 */
interface ColorUV {
    companion object {
        /**
         * Creates a new function implementation.
         */
        inline operator fun invoke(crossinline block: (u: Float, v: Float) -> Float) =
            object : ColorUV {
                override fun invoke(u: Float, v: Float) = block(u, v)
            }
    }

    operator fun invoke(u: Float, v: Float): Float
}

/**
 * A color function evaluated at absolute coordinates.
 */
interface ColorAbs {
    companion object {
        /**
         * Creates a new function implementation.
         */
        inline operator fun invoke(crossinline block: (x: Float, y: Float) -> Float) =
            object : ColorAbs {
                override fun invoke(x: Float, y: Float) = block(x, y)
            }
    }

    operator fun invoke(x: Float, y: Float): Float
}

/**
 * Returns a function taking absolute coordinates in the given space.
 */
fun ColorUV.toAbs(x: Float, y: Float, width: Float, height: Float) = ColorAbs { ax: Float, ay: Float ->
    this@toAbs((ax - x) / width, (ay - y) / height)
}

/**
 * Returns a function taking u/v coordinates in the given space.
 */
fun ColorAbs.toUv(x: Float, y: Float, width: Float, height: Float) = ColorUV { u: Float, v: Float ->
    this@toUv(x + width * u, y + height * v)
}

/**
 * A linear color function that is constant.
 */
fun Color.solid(): ColorUV {
    val bits = toFloatBits()
    return ColorUV { _, _ -> bits }
}

/**
 * Unit gradient in horizontal direction from left.
 */
fun gradientLeft(from: Color, to: Color): ColorUV {
    val capture = Color()
    return ColorUV { u, _ -> capture.set(from).lerp(to, u.coerceIn(0f, 1f)).toFloatBits() }
}

/**
 * Unit gradient in vertical direction from bottom.
 */
fun gradientBottom(from: Color, to: Color): ColorUV {
    val capture = Color()
    return ColorUV { _, v ->
        capture.set(from).lerp(to, v.coerceIn(0f, 1f)).toFloatBits()
    }
}

/**
 * Unit gradient in horizontal direction from right.
 */
fun gradientRight(from: Color, to: Color): ColorUV {
    val capture = Color()
    return ColorUV { u, _ -> capture.set(from).lerp(to, (1f - u).coerceIn(0f, 1f)).toFloatBits() }
}

/**
 * Unit gradient in vertical direction from top.
 */
fun gradientTop(from: Color, to: Color): ColorUV {
    val capture = Color()
    return ColorUV { _, v ->
        capture.set(from).lerp(to, (1f - v).coerceIn(0f, 1f)).toFloatBits()
    }
}