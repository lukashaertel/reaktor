package eu.metatools.reaktor.ex

import eu.metatools.reaktor.reentrantQueue

fun <T> hostRoot(set: (T) -> Unit, generate: () -> T): () -> Unit {
    // Reentry handle.
    var handle = {}

    // Create routine.
    handle = reentrantQueue(set, {
        amendInvalidation(handle) {
            hostEffects {
                generate()
            }
        }
    })

    return handle
}