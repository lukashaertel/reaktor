package eu.metatools.reaktor

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun reconcileNode(
    initialVirtual: Any? = null,
    initialActual: Any? = null,
    crossinline update: (Any?) -> Unit,
) = object : ReadWriteProperty<Any?, Any?> {
    private var lastVirtual: Any? = initialVirtual
    private var lastActual: Any? = initialActual

    override fun getValue(thisRef: Any?, property: KProperty<*>) = lastVirtual
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Any?) {
        if (lastVirtual == value)
            return

        val newActual = reconcile(lastVirtual, value, lastActual)
        lastVirtual = value
        if (newActual != lastActual) {
            lastActual = newActual
            update(newActual)
        }
    }

}