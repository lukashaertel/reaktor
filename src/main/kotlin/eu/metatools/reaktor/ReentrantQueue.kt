package eu.metatools.reaktor

private val rerun = ThreadLocal.withInitial<Boolean?> { null }

/**
 * I yiked.
 */
fun isRerunSet() = rerun.get() == true

// Flattened recursive fixpoint. No idea how this fits in but ya, think that does the trick.
fun <T> reentrantQueue(yield: (T) -> Unit, block: () -> T): () -> Unit {
    return {
        if (rerun.get() != null) {
            rerun.set(true)
        } else {
            try {
                rerun.set(false)
                yield(block())

                while (rerun.get()) {
                    rerun.set(false)
                    yield(block())
                }
            } finally {
                rerun.set(null)
            }
        }
    }
}