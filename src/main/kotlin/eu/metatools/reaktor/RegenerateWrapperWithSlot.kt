package eu.metatools.reaktor

/**
 * Thread local to pass implicit.
 */
private val currentHandleThreadLocal = ThreadLocal<() -> Unit>()

/**
 * Gets the current handle. Should be retrieved at generate level, using it within event handlers etc. will likely
 * result in non-assigned values.
 */
fun currentHandle(): () -> Unit = currentHandleThreadLocal.get()

/**
 * Implicit variant of [regenerateWrapper]. The handle is available on [generate] through [currentHandle].
 */
fun regenerateWrapperWithSlot(set: (Any?) -> Unit, generate: () -> Any?) =
    regenerateWrapper(set, {
        currentHandleThreadLocal.set(it)
        try {
            generate()
        } finally {
            currentHandleThreadLocal.remove()
        }
    })