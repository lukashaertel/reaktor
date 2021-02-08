package eu.metatools.reaktor.ex


/**
 * Effects by location.
 */
private val effects = HashMap<Any, Pair<Any?, () -> Unit>>()

/**
 * Dead-flags.
 */
private val flagged = HashSet<Any>()

/**
 * Surround block that will use [useEffect] or [useEffectWithDispose].
 */
fun <T> hostEffects(block: () -> T): T {
    // Reset dead flags to all active effects.
    flagged.clear()
    flagged.addAll(effects.keys)

    try {
        // Run and return from block.
        return block()
    } finally {
        // For everything not flagged, dispose.
        for (key in flagged)
            effects.remove(key)?.second?.invoke()
    }
}

/**
 * Values are reference equal.
 */
val refEq = { a: Any?, b: Any? -> a === b }

/**
 * Values are equal as per their definition.
 */
val shallowEq = { a: Any?, b: Any? -> a == b }

/**
 * Uses an effect. When [deps] change (as per [equality]), [block] is executed. If deps are not given, the block is only
 * executed on first creation. When the block is executed again or the effect is no longer used, the result of the last
 * invocation (the dispose part), is invoked.
 */
fun useEffectWithDispose(deps: Any? = null, equality: (Any?, Any?) -> Boolean = shallowEq, block: () -> (() -> Unit)) {
    // Get current location.
    val location = localize()

    // Flag as used.
    flagged.remove(location)

    // Get previous value.
    val previous = effects[location]

    // Check if previous unassigned.
    if (previous == null) {
        // Unassigned, run directly.
        effects[location] = deps to block()
    } else if (!equality(deps, previous.first)) {
        // Assigned, dispose previous.
        previous.second.invoke()

        // Run new, disconnect disposal and set deps to prevent reflow.
        effects[location] = deps to {}
        effects[location] = deps to block()
    }
}

/**
 * Uses an effect. When [deps] change (as per [equality]), [block] is executed. If deps are not given, the block is only
 * executed on first creation. No disposal is performed.
 */
fun useEffect(deps: Any? = null, equality: (Any?, Any?) -> Boolean = shallowEq, block: () -> Unit) {
    useEffectWithDispose(deps, equality) {
        // Run block, return empty.
        block(); { }
    }
}
