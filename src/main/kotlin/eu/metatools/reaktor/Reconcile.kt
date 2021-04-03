package eu.metatools.reaktor


/**
 * Applies the difference between [vFrom] and [vTo] to [on] and returns the result.
 */
fun reconcile(vFrom: Any?, vTo: Any?, on: Any?): Any? {
    // Target null, return null.
    if (vTo == null) {
        // If source was not null, release it with the actual object.
        if (vFrom != null)
            release(vFrom, on)
        return null
    }

    // Source null, needs to make new. No release needed as from was not given.
    if (vFrom == null)
        return make(vTo)

    // Unchanged, return. No release needed, same object.
    if (vFrom == vTo)
        return on

    // On should be sync with from, mark not null.
    on as Any

    // Target is virtual, continue with virtual diff.
    if (vTo is Virtual<*>) {
        // From is not virtual, needs to make new. Release previous with actual object.
        if (vFrom !is Virtual<*>) {
            release(vFrom, on)
            return vTo.make()
        }

        // Type different, make new. Release previous with actual object.
        if (vFrom::class != vTo::class) {
            release(vFrom, on)
            return vTo.make()
        }

        // Mark as virtual of any.
        @Suppress("unchecked_cast")
        vTo as Virtual<Any>

        // Begin reconcile, mark to actual.
        vTo.begin(on)

        // Diff all properties.
        for (i in 0 until vTo.props) {
            // Get all children.
            val vChildFrom = vFrom.getOwn(i)
            val vChildTo = vTo.getOwn(i)
            val actualProp = vTo.getActual(i, on)

            // Diff for update.
            val update = reconcile(vChildFrom, vChildTo, actualProp)

            // If update is different, set.
            if (actualProp !== update)
                vTo.updateActual(i, on, update)
        }

        // End reconcile, mark to actual.
        vTo.end(on)

        // Return current value. No release needed because it was within reconcile.
        return on
    }

    // Target is map, continue with map diff.
    if (vTo is Map<*, *>) {
        if (vFrom !is Map<*, *> || on !is MutableMap<*, *>) {
            release(vFrom, on)
            return make(vTo)
        }
        // From is list, on should have it then.
        @Suppress("unchecked_cast")
        on as MutableMap<Any?, Any?>

        // Iterate first part of union.
        for (key in vFrom.keys)
            if (key in vTo) {
                // In both. Check if changed, otherwise skip.
                if (vFrom[key] == vTo[key])
                    continue

                // Changed, apply update.
                val update = reconcile(vFrom[key], vTo[key], on[key])
                if (update !== on[key])
                    on[key] = update
            } else {
                // In from but not in to. Remove.
                on.remove(key)
            }

        // Iterate second part of union.
        for (key in vTo.keys)
            if (key !in vFrom) {
                // Not in from, make.
                on[key] = make(vTo[key])
            }

        // Return current value.
        return on
    }

    // Target is list, continue with list diff.
    if (vTo is List<*>) {
        if (vFrom !is List<*> || on !is MutableList<*>) {
            release(vFrom, on)
            return make(vTo)
        }

        // From is list, on should have it then.
        @Suppress("unchecked_cast")
        on as MutableList<Any?>

        // Perform reordering based on present keys or reference. Create by make and merge by reconcile.
        reorderMergeByKey(vFrom, vTo, on, ::match, ::make, ::release) { f, t, o ->
            reconcile(f, t, o)
        }

        // Return current value.
        return on
    }

    // Target is set, continue with set diff.
    if (vTo is Set<*>)
        TODO("Map not supported yet")

    // Return target, no special diff processing.
    return vTo
}

