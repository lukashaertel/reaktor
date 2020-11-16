package eu.metatools.kvr.gdx

import java.util.*

private val contents = ThreadLocal.withInitial {
    Stack<MutableList<*>>()
}

private fun <T> tryPushContent(element: T): T {
    if (element !is Any)
        return element

    // Get stack.
    @Suppress("non_public_call_from_public_inline")
    val stack = contents.get()

    if (stack.isEmpty())
        return element

    @Suppress("member_projected_out")
    stack.peek().add(element)
    return element
}

// TODO: DSL Marker

/**
 * Constructs an item with no child elements.
 */
inline fun <R> constructTerminal(create: () -> R): R {
    // Return the created item.
    @Suppress("non_public_call_from_public_inline")
    return tryPushContent(create())
}

/**
 * Constructs an item with child elements.
 */
inline fun <E, R> constructParent(content: () -> Unit, create: (List<E>) -> R): R {
    // Get stack.
    @Suppress("non_public_call_from_public_inline")
    val stack = contents.get()

    // Create item holder.
    val items = mutableListOf<E>()

    // Create content to new list.
    stack.push(items)
    content()
    stack.pop()

    // Return the created item.
    @Suppress("non_public_call_from_public_inline")
    return tryPushContent(create(items))
}

fun <A, R> component(fn: (A) -> R): (A) -> R {
    var lastPresent = false
    var lastArg: A? = null
    var lastResult: R? = null
    return { arg ->
        if (lastPresent && lastArg == arg)
            @Suppress("non_public_call_from_public_inline", "unchecked_cast")
            tryPushContent(lastResult as R)
        else
            fn(arg).also {
                lastPresent = true
                lastArg = arg
                lastResult = it
            }
    }
}

fun <R> component(fn: () -> R): () -> R {
    val actual = component<Unit, R> { fn() }
    return { actual(Unit) }
}

fun <A1, A2, R> component(fn: (A1, A2) -> R): (A1, A2) -> R {
    val actual = component<List<Any?>, R> { (arg1, arg2) ->
        @Suppress("unchecked_cast")
        fn(arg1 as A1, arg2 as A2)
    }
    return { a1, a2 -> actual(listOf(a1, a2)) }
}

fun <A1, A2, A3, R> component(fn: (A1, A2, A3) -> R): (A1, A2, A3) -> R {
    val actual = component<List<Any?>, R> { (arg1, arg2, arg3) ->
        @Suppress("unchecked_cast")
        fn(arg1 as A1, arg2 as A2, arg3 as A3)
    }
    return { a1, a2, a3 -> actual(listOf(a1, a2, a3)) }
}


fun <A1, A2, A3, A4, R> component(fn: (A1, A2, A3, A4) -> R): (A1, A2, A3, A4) -> R {
    val actual = component<List<Any?>, R> { (arg1, arg2, arg3, arg4) ->
        @Suppress("unchecked_cast")
        fn(arg1 as A1, arg2 as A2, arg3 as A3, arg4 as A4)
    }
    return { a1, a2, a3, a4 -> actual(listOf(a1, a2, a3, a4)) }
}

fun <A1, A2, A3, A4, A5, R> component(fn: (A1, A2, A3, A4, A5) -> R): (A1, A2, A3, A4, A5) -> R {
    val actual = component<List<Any?>, R> { (arg1, arg2, arg3, arg4, arg5) ->
        @Suppress("unchecked_cast")
        fn(arg1 as A1, arg2 as A2, arg3 as A3, arg4 as A4, arg5 as A5)
    }
    return { a1, a2, a3, a4, a5 -> actual(listOf(a1, a2, a3, a4, a5)) }
}