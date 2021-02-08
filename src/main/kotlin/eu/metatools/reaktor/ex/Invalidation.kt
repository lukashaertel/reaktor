package eu.metatools.reaktor.ex

/**
 * Invalidation block.
 */
private val invalidationBlock = ThreadLocal.withInitial { {} }

/**
 * Amends invalidation by the given [invalidation] method, within the scope of the [block].
 */
fun <T> amendInvalidation(invalidation: () -> Unit, block: () -> T): T {
    // Get for reset and invocation.
    val previous = invalidationBlock.get()

    // Amend.
    invalidationBlock.set {
        invalidation()
        previous()
    }

    try {
        // Return from block.
        return block()
    } finally {
        // Revert.
        invalidationBlock.set(previous)
    }
}

/**
 * Returns the current invalidator.
 */
fun invalidator(): () -> Unit =
    invalidationBlock.get()

/**
 * Runs the current invalidation block.
 */
fun invalidate() =
    invalidator()()