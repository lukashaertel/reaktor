package eu.metatools.reaktor

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * Returns a location for the current invocation point.
 */
fun localize() =
    // 😬😬😬😬😬😬😬😬
    Thread.currentThread().stackTrace.drop(1).takeWhile { it.fileName != "RegenerateWrapperWithSlot.kt" }.toList()

/**
 * States by location.
 */
private val states = ThreadLocal.withInitial { HashMap<Any, Any?>() }

/**
 * Introduces a local state. When the state is changed, the implicit regeneration handle slot is invoked. See
 * [regenerateWrapperWithSlot] and [regenerateWrapperWithSlotAndEffects].
 */
fun <T> useState(initial: T): ReadWriteProperty<Nothing?, T> {
    val states = states.get()

    val handle = currentHandle()
    val location = localize().toString()

    states.computeIfAbsent(location) {
        initial
    }

    @Suppress("unchecked_cast")
    return object : ReadWriteProperty<Nothing?, T> {
        override fun getValue(thisRef: Nothing?, property: KProperty<*>): T {
            return states[location] as T
        }

        override fun setValue(thisRef: Nothing?, property: KProperty<*>, value: T) {
            val original = states[location] as T
            if (original != value) {
                states[location] = value
                handle()
            }
        }
    }
}

/**
 * Effects by location.
 */
private val effects = ThreadLocal.withInitial { HashMap<Any, Pair<Any?, () -> Unit>>() }

/**
 * Dead-flags.
 */
private val flagged = ThreadLocal.withInitial { HashSet<Any>() }

/**
 * Surround block that will use [useEffect] or [useEffectWithDispose].
 */
fun <T> hostEffects(block: () -> T): T {
    // Get host maps.
    val flagged = flagged.get()
    val effects = effects.get()

    // Reset dead flags to all active effects.
    flagged.clear()
    flagged.addAll(effects.keys)

    try {
        // Run and return from block.
        return block()
    } finally {
        // For everything not flagged, dispose.
        for (key in flagged)
            effects.getValue(key).second.invoke()
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
    // Get host maps.
    val flagged = flagged.get()
    val effects = effects.get()

    // Get current location.
    val location = localize().toString()

    // Flag as used.
    flagged.add(location)

    // Get previous value.
    val previous = effects[location]

    // Check if previous unassigned.
    if (previous == null) {
        // Unassigned, run directly.
        effects[location] = deps to block()
    } else if (!equality(deps, previous.first)) {
        // Assigned, dispose previous.
        previous.second.invoke()

        // Run new.
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

/**
 * Variant of [regenerateWrapperWithSlot] that supports [useEffect].
 */
fun regenerateWrapperWithSlotAndEffects(set: (Any?) -> Unit, generate: () -> Any?) =
    regenerateWrapperWithSlot(set) {
        hostEffects {
            generate()
        }
    }