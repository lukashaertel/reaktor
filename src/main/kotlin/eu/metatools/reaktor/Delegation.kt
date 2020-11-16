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
     * @param getter The element getter.
     * @param setter The element setter.
     * @param add Element addition.
     * @param remove Element removal by index.
     */
    fun <A, E> list(
        actual: A, prop: Int,
        size: A.() -> Int,
        getter: A.(Int) -> Any?,
        setter: A.(Int, E) -> Unit,
        add: A.(E) -> Unit,
        remove: A.(Int) -> Unit
    ): MutableListStub {
        // Get generated lists for actual object.
        val top = lists.getOrPut(actual, ::HashMap)

        // Get by key, or make new skeletal implementation.
        return top.getOrPut(prop) {
            object : MutableListStub {
                override val size
                    get() = actual.size()

                override fun add(element: Any?): Boolean {
                    @Suppress("unchecked_cast")
                    actual.add(element as E)
                    return false
                }

                override fun get(index: Int): Any? {
                    return actual.getter(index)
                }

                override fun set(index: Int, element: Any?): Any? {
                    @Suppress("unchecked_cast")
                    actual.setter(index, element as E)
                    return Unit
                }

                override fun removeAt(index: Int): Any? {
                    actual.remove(index)
                    return Unit
                }
            }
        }

    }
}