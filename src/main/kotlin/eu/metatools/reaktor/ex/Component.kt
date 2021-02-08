package eu.metatools.reaktor.ex

import eu.metatools.reaktor.isRerunSet

private val componentResults = HashMap<Any, Pair<Any?, Any?>>()

fun <T> defineComponent(block: (args: Any?) -> T) = { args: Any? ->
    // Get location.
    val location = localize()

    // Remove the location from computed results on invalidation.
    amendInvalidation({ componentResults.remove(location) }) {
        // Get existing result.
        var existing = componentResults[location]

        // If not yet computed or mismatching arguments, recompute.
        if (existing == null || existing.first != args) {
            existing = args to block(args)
            if (isRerunSet())
                componentResults.remove(location)
            else
                componentResults[location] = existing
        }

        // Return computed value.
        @Suppress("unchecked_cast")
        existing.second as T
    }
}

inline fun <T> component(crossinline block: () -> T): () -> T {
    // Bind directly, no casts needed.
    val actual = defineComponent { block() }

    // Return wrapper.
    return { actual(Unit) }
}

inline fun <A1, T> component(crossinline block: (A1) -> T): (A1) -> T {
    // Bind with casts.
    val actual = defineComponent {
        // Cast arguments.
        @Suppress("unchecked_cast")
        it as A1

        // Return block results.
        block(it)
    }

    // Return wrapper.
    return { actual(it) }
}

inline fun <A1, A2, T> component(crossinline block: (A1, A2) -> T): (A1, A2) -> T {
    // Bind with casts.
    val actual = defineComponent {
        @Suppress("unchecked_cast")
        it as Pair<A1, A2>
        block(it.first, it.second)
    }

    // Return wrapper.
    return { a1, a2 -> actual(a1 to a2) }
}


inline fun <A1, A2, A3, T> component(crossinline block: (A1, A2, A3) -> T): (A1, A2, A3) -> T {
    // Bind with casts.
    val actual = defineComponent {
        @Suppress("unchecked_cast")
        it as Triple<A1, A2, A3>
        block(it.first, it.second, it.third)
    }

    // Return wrapper.
    return { a1, a2, a3 -> actual(Triple(a1, a2, a3)) }
}

inline fun <A1, A2, A3, A4, T> component(crossinline block: (A1, A2, A3, A4) -> T): (A1, A2, A3, A4) -> T {
    // Bind with casts.
    val actual = defineComponent {
        @Suppress("unchecked_cast")
        it as Triple<A1, A2, Pair<A3, A4>>
        block(it.first, it.second, it.third.first, it.third.second)
    }

    // Return wrapper.
    return { a1, a2, a3, a4 -> actual(Triple(a1, a2, a3 to a4)) }
}

inline fun <A1, A2, A3, A4, A5, T> component(crossinline block: (A1, A2, A3, A4, A5) -> T): (A1, A2, A3, A4, A5) -> T {
    // Bind with casts.
    val actual = defineComponent {
        @Suppress("unchecked_cast")
        it as Triple<A1, A2, Triple<A3, A4, A5>>
        block(it.first, it.second, it.third.first, it.third.second, it.third.third)
    }

    // Return wrapper.
    return { a1, a2, a3, a4, a5 -> actual(Triple(a1, a2, Triple(a3, a4, a5))) }
}