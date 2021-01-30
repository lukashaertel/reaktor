package eu.metatools.reaktor

/**
 * Passes a handle to [generate] that allows for re-generating the outcome.
 * @param <T> The type of the outcome.
 * @param set Receives the outcome.
 * @param generate Generates the value, receives the handle to trigger the cycle again.
 * @return Returns the regeneration handler.
 */
fun <T> regenerate(set: (T) -> Unit, generate: (() -> Unit) -> T): () -> Unit {
    var handle: () -> Unit = {}
    handle = { set(generate(handle)) }
    return handle
}