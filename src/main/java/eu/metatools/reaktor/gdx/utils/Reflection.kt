package eu.metatools.reaktor.gdx.utils

import java.lang.reflect.Field

class Hidden<T, R>(private val field: Field) {
    operator fun invoke(t: T): R {
        @Suppress("unchecked_cast")
        return field.get(t) as R
    }

    operator fun invoke(t: T, value: R) {
        field.set(t, value)
    }
}

inline fun <reified T, R> hidden(name: String): Hidden<T, R> {
    val field = T::class.java.getDeclaredField(name)
    field.isAccessible = true
    return Hidden(field)
}