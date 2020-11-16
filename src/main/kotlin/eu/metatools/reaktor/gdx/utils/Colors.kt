package eu.metatools.reaktor.gdx.utils

import com.badlogic.gdx.graphics.Color
import java.util.*

private val createdColors = WeakHashMap<String, Color>()
val String.hex get() = createdColors.getOrPut(this) { Color.valueOf(this) }