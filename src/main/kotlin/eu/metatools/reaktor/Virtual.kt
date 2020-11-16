package eu.metatools.reaktor

interface Virtual<A : Any> {
    /**
     * Makes a new actual object.
     */
    fun make(): A

    /**
     * Number of properties.
     */
    val props: Int

    /**
     * Gets the property at index [prop] for the [Virtual] object.
     */
    fun getOwn(prop: Int): Any?

    /**
     * Gets the property at index [prop] for the [actual] object.
     *
     * For [List]s: if the target has alternative methods to mutate the property [prop], [Delegation.list] can be used
     * to make or retrieve a method wrapper.
     */
    fun getActual(prop: Int, actual: A): Any?

    /**
     * Updates the property at index [prop] for the [actual] object to the given [value]. If a property cannot be
     * changed and is only initialized by [make], this may throw an [UnsupportedOperationException].
     */
    fun updateActual(prop: Int, actual: A, value: Any?)

    /**
     * Called at the start of reconciliation with actual.
     */
    fun begin(actual: A) = Unit

    /**
     * Called at the end of reconciliation with actual.
     */
    fun end(actual: A) = Unit

    fun release(actual: A) = Unit
}

/**
 * True if equal with regard to properties only.
 */
fun Virtual<*>.propsEqual(other: Virtual<*>): Boolean {
    repeat(props) {
        if (getOwn(it) != other.getOwn(it))
            return false
    }

    return true
}