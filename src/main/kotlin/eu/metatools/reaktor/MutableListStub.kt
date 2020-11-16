package eu.metatools.reaktor

/**
 * Overrides all methods of [MutableList] by throwing an [UnsupportedOperationException].
 */
interface MutableListStub : MutableList<Any?> {
    override val size: Int
        get() = throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun contains(element: Any?): Boolean =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun containsAll(elements: Collection<Any?>): Boolean =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun get(index: Int): Any? =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun indexOf(element: Any?): Int =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun isEmpty(): Boolean =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun iterator(): MutableIterator<Any?> =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun lastIndexOf(element: Any?): Int =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun add(element: Any?): Boolean =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun add(index: Int, element: Any?): Unit =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun addAll(index: Int, elements: Collection<Any?>): Boolean =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun addAll(elements: Collection<Any?>): Boolean =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun clear(): Unit =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun listIterator(): MutableListIterator<Any?> =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun listIterator(index: Int): MutableListIterator<Any?> =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun remove(element: Any?): Boolean =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun removeAll(elements: Collection<Any?>) =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun removeAt(index: Int): Any? =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun retainAll(elements: Collection<Any?>): Boolean =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun set(index: Int, element: Any?): Any? =
        throw UnsupportedOperationException("Unsupported operation for stub.")

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Any?> =
        throw UnsupportedOperationException("Unsupported operation for stub.")
}