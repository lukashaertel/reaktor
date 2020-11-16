package eu.metatools.kvr

fun reconcileWrapper(updated: (Any?) -> Unit): (Any?) -> Unit {
    var lastVirtual: Any? = null
    var lastActual: Any? = null
    return {
        val newActual = reconcile(lastVirtual, it, lastActual)
        lastVirtual = it
        if (newActual != lastActual) {
            lastActual = newActual
            updated(newActual)
        }
    }
}