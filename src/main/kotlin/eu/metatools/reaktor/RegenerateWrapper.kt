package eu.metatools.reaktor

/**
 * Reconciles generated values with the previous run. The generator gets passed a handle that will trigger
 * regeneration. See [regenerate] for details.
 * @param set Run when a [reconcile] result has been produced.
 * @param generate Generates the virtual part for [reconcile].
 * @return Returns the generate handle.
 */
fun regenerateWrapper(set: (Any?) -> Unit, generate: (() -> Unit) -> Any?): () -> Unit {
    var lastVirtual: Any? = null
    var lastActual: Any? = null

    return regenerate({
        val newActual = reconcile(lastVirtual, it, lastActual)
        lastVirtual = it
        if (newActual != lastActual) {
            lastActual = newActual
            set(newActual)
        }
    }, generate)
}

