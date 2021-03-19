package eu.metatools.reaktor.gdx.utils

import com.badlogic.gdx.graphics.Color
import java.util.*

private val createdColors = WeakHashMap<String, Color>()
val String.hex get() = createdColors.getOrPut(this) { Color.valueOf(this) }

private val floats = FloatArray(3)

val Color.darker: Color
    get() {
        toHsv(floats)
        floats[2] = maxOf(0f, floats[2] * 0.8f)
        return cpy().fromHsv(floats)
    }

val Color.brighter: Color
    get() {
        toHsv(floats)
        floats[2] = minOf(1f, floats[2] / 0.8f)
        return cpy().fromHsv(floats)
    }