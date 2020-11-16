package eu.metatools.kvr

/**
 * Makes the actual version of [v]. If [v] is virtual, their [Virtual.make] method is invoked for the result. [Map]s,
 * [List]s and [Set]s are mapped to their mutable counterparts.
 */
fun make(v: Any?): Any? {
    // Null will return null.
    if (v == null)
        return null

    // Virtual is responsible for it's own make.
    if (v is Virtual<*>)
        @Suppress("unchecked_cast")
        return (v as Virtual<Any>).make()

    // Map mapped to mutable map.
    if (v is Map<*, *>)
        v.entries.associateTo(mutableMapOf()) { make(it.key) to make(it.value) }

    // List mapped to mutable list.
    if (v is List<*>)
        v.mapTo(mutableListOf()) { make(it) }

    // Set mapped to mutable set.
    if (v is Set<*>)
        v.mapTo(mutableSetOf()) { make(it) }

    // Return value, no special materialization.
    return v
}