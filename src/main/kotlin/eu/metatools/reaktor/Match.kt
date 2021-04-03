package eu.metatools.reaktor

/**
 * Checks if [a] and [b] are candidates for merging due to equal key.
 */
fun match(unmoved: Boolean, a: Any?, b: Any?) =
    a === b || (a is Virtual<*> && b is Virtual<*> && a::class == b::class &&
            if (a.key == null && b.key == null)
                unmoved
            else
                a.key == b.key)
