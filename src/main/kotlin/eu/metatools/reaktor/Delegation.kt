package eu.metatools.reaktor

import java.util.*
import kotlin.collections.HashMap

/**
 * Delegates the core operations of [reconcile] to a skeletal implementation.
 */
object Delegation {
    /**
     * All generated lists.
     */
    private val lists = WeakHashMap<Any?, MutableMap<Int, MutableListStub>>()

    /**
     * Generates or retrieves a list implementation.
     * @param actual The actual object to work on.
     * @param prop The key of the list (if multiple).
     * @param size The size getter.
     * @param get The element getter.
     * @param set The element setter.
     * @param add Element addition.
     * @param addAt Element insertion.
     * @param removeAt Element removal by index.
     */
    fun <A, E> list(
        actual: A, prop: Int,
        size: A.() -> Int,
        get: A.(index: Int) -> E,
        set: A.(index: Int, E) -> E,
        add: A.(element: E) -> Boolean,
        addAt: A.(index: Int, element: E) -> Unit,
        removeAt: A.(index: Int) -> E?,
    ): MutableListStub {
        val top = lists.getOrPut(actual, ::HashMap)

        return top.getOrPut(prop) {
            object : MutableListStub {
                override val size get() = actual.size()

                override fun add(element: Any?) =
                    @Suppress("unchecked_cast")
                    actual.add(element as E)

                override fun add(index: Int, element: Any?) =
                    @Suppress("unchecked_cast")
                    actual.addAt(index, element as E)

                override fun get(index: Int) =
                    actual.get(index)

                override fun set(index: Int, element: Any?) =
                    @Suppress("unchecked_cast")
                    actual.set(index, element as E)

                override fun removeAt(index: Int) =
                    actual.removeAt(index)

                override fun hashCode(): Int {
                    var result = 7
                    for (i in 0 until actual.size())
                        result = 31 * result + actual.get(i).hashCode()
                    return result
                }

                override fun equals(other: Any?): Boolean {
                    if (this === other) return true
                    if (other === null) return false

                    if (other !is List<*>)
                        return false

                    if (actual.size() != other.size)
                        return false

                    for (i in 0 until other.size)
                        if (actual.get(i) != other[i])
                            return false

                    return true
                }

                override fun iterator() = object : MutableIterator<Any?> {
                    var at = 0
                    var end = actual.size()
                    override fun hasNext() = at < end

                    override fun next(): Any? {
                        val result = actual.get(at)
                        at++
                        return result
                    }

                    override fun remove() {
                        actual.removeAt(at.dec())
                        at--
                        end--
                    }
                }

                override fun toString() =
                    actual.toString()
            }
        }
    }
}