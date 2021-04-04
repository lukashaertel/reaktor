package eu.metatools.reaktor

abstract class Virtual<A : Any> {
    /**
     * The key for reconcile matching in lists.
     */
    abstract val key: Any?

    /**
     * Makes a new actual object.
     */
    abstract fun make(): A

    /**
     * Number of properties.
     */
    abstract val props: Int

    /**
     * Gets the property at index [prop] for the [Virtual] object.
     */
    abstract fun getOwn(prop: Int): Any?

    /**
     * Gets the property at index [prop] for the [actual] object.
     *
     * For [List]s: if the target has alternative methods to mutate the property [prop], [Delegation.list] can be used
     * to make or retrieve a method wrapper.
     */
    abstract fun getActual(prop: Int, actual: A): Any?

    /**
     * Updates the property at index [prop] for the [actual] object to the given [value]. If a property cannot be
     * changed and is only initialized by [make], this may throw an [UnsupportedOperationException].
     */
    abstract fun updateActual(prop: Int, actual: A, value: Any?)

    /**
     * Called at the start of reconciliation with actual.
     */
    open fun begin(actual: A) = Unit

    /**
     * Called at the end of reconciliation with actual.
     */
    open fun end(actual: A) = Unit

    /**
     * Releases the instance.
     */
    open fun release(actual: A) = Unit

    /**
     * Checks if the [prop] is equally assigned in the [other] virtual. Can be overridden if properties do not provide
     * true quality and only reference equality.
     */
    open fun propEqual(prop: Int, other: Virtual<*>) =
        getOwn(prop) == other.getOwn(prop)

//    override fun hashCode(): Int {
//        var result = 13
//        for (i in 0 until props)
//            result = 17 * result + getOwn(i).hashCode()
//        return result
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other === null) return false
//
//        if (this::class != other::class)
//            return false
//
//        other as Virtual<*>
//
//        for (i in 0 until props)
//            if (getOwn(i) != other.getOwn(i))
//                return false
//
//        return true
//    }
}