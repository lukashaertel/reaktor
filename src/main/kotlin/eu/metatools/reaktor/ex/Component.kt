package eu.metatools.reaktor.ex

import eu.metatools.reaktor.gdx.utils.runWithReceiver
import eu.metatools.reaktor.gdx.utils.tryReceive
import eu.metatools.reaktor.isRerunSet

private val componentResults = HashMap<Any, Pair<Any?, Any?>>()

/**
 * Generates the component value. Will use current key to amend the localization. The [args] are checked for changes
 * to return the same result. The [block] is run for the new result, if args changed or no existing result is present.
 */
private inline fun <T> generateComponent(args: Any?, crossinline block: () -> T): T {
    // Get location.
    val location = localize() + peekKey()

    // Remove the location from computed results on invalidation.
    return amendInvalidation({ componentResults.remove(location) }) {
        // Get existing result.
        var existing = componentResults[location]

        // If not yet computed or mismatching arguments, recompute.
        if (existing == null || existing.first != args) {
            existing = args to block()
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

/**
 * Defines a zero-argument component.
 */
fun <T> component(block: () -> T) = WithKey0 {
    // Generate component on invoke. Use parameter-stand in object.
    generateComponent(Unit) {
        // Reset the receiver for the block so that invocation does not leak.
        runWithReceiver(null) { block() }
    }.tryReceive()
}

/**
 * Defines a one-argument component.
 */
fun <A1, T> component(block: (A1) -> T) = WithKey1 { a1: A1 ->
    // Generate component on invoke. Use parameter-stand in object.
    generateComponent(a1) {
        // Reset the receiver for the block so that invocation does not leak.
        runWithReceiver(null) { block(a1) }
    }.tryReceive()
}

/**
 * Defines a two-argument component.
 */
fun <A1, A2, T> component(block: (A1, A2) -> T) = WithKey2 { a1: A1, a2: A2 ->
    // Generate component on invoke. Use parameter-stand in object.
    generateComponent(a1 to a2) {
        // Reset the receiver for the block so that invocation does not leak.
        runWithReceiver(null) { block(a1, a2) }
    }.tryReceive()
}

/**
 * Defines a three-argument component.
 */
fun <A1, A2, A3, T> component(block: (A1, A2, A3) -> T) = WithKey3 { a1: A1, a2: A2, a3: A3 ->
    // Generate component on invoke. Use parameter-stand in object.
    generateComponent(Triple(a1, a2, a3)) {
        // Reset the receiver for the block so that invocation does not leak.
        runWithReceiver(null) { block(a1, a2, a3) }
    }.tryReceive()
}

/**
 * Defines a four-argument component.
 */
fun <A1, A2, A3, A4, T> component(block: (A1, A2, A3, A4) -> T) = WithKey4 { a1: A1, a2: A2, a3: A3, a4: A4 ->
    // Generate component on invoke. Use parameter-stand in object.
    generateComponent(Triple(a1, a2, a3 to a4)) {
        // Reset the receiver for the block so that invocation does not leak.
        runWithReceiver(null) { block(a1, a2, a3, a4) }
    }.tryReceive()
}