package eu.metatools.reaktor.ex

/**
 * Local thread local value for currently assigned key.
 */
private val currentKeyLocal = ThreadLocal.withInitial<Any?> { null }

/**
 * Views the key without removing it.
 */
fun peekKey(): Any? = currentKeyLocal.get()

/**
 * Views and resets the key to null.
 */
fun consumeKey(): Any? = currentKeyLocal.get().also {
    currentKeyLocal.set(null)
}

/**
 * Function that can be invoked with a key.
 */
class WithKey0<out T>(val block: () -> T) {
    /**
     * Runs the block with the default key.
     */
    operator fun invoke() = invoke(null)

    /**
     * Runs the block with the given key.
     * @param key Unique key for this invocation.
     */
    operator fun invoke(key: Any?): T {
        val previous = currentKeyLocal.get()

        return try {
            currentKeyLocal.set(key)
            block()
        } finally {
            currentKeyLocal.set(previous)
        }
    }
}

/**
 * Function that can be invoked with a key.
 */
class WithKey1<in A1, out T>(val block: (A1) -> T) {
    /**
     * Runs the block with the default key.
     */
    operator fun invoke(arg1: A1) = invoke(null, arg1)

    /**
     * Runs the block with the given key.
     * @param key Unique key for this invocation.
     */
    operator fun invoke(key: Any?, arg1: A1): T {
        val previous = currentKeyLocal.get()

        return try {
            currentKeyLocal.set(key)
            block(arg1)
        } finally {
            currentKeyLocal.set(previous)
        }
    }
}

/**
 * Function that can be invoked with a key.
 */
class WithKey2<in A1, in A2, out T>(val block: (A1, A2) -> T) {
    /**
     * Runs the block with the default key.
     */
    operator fun invoke(arg1: A1, arg2: A2) = invoke(null, arg1, arg2)

    /**
     * Runs the block with the given key.
     * @param key Unique key for this invocation.
     */
    operator fun invoke(key: Any?, arg1: A1, arg2: A2): T {
        val previous = currentKeyLocal.get()

        return try {
            currentKeyLocal.set(key)
            block(arg1, arg2)
        } finally {
            currentKeyLocal.set(previous)
        }
    }
}

/**
 * Function that can be invoked with a key.
 */
class WithKey3<in A1, in A2, in A3, out T>(val block: (A1, A2, A3) -> T) {
    /**
     * Runs the block with the default key.
     */
    operator fun invoke(arg1: A1, arg2: A2, arg3: A3) = invoke(null, arg1, arg2, arg3)

    /**
     * Runs the block with the given key.
     * @param key Unique key for this invocation.
     */
    operator fun invoke(key: Any?, arg1: A1, arg2: A2, arg3: A3): T {
        val previous = currentKeyLocal.get()

        return try {
            currentKeyLocal.set(key)
            block(arg1, arg2, arg3)
        } finally {
            currentKeyLocal.set(previous)
        }
    }
}

/**
 * Function that can be invoked with a key.
 */
class WithKey4<in A1, in A2, in A3, in A4, out T>(val block: (A1, A2, A3, A4) -> T) {
    /**
     * Runs the block with the default key.
     */
    operator fun invoke(arg1: A1, arg2: A2, arg3: A3, arg4: A4) = invoke(null, arg1, arg2, arg3, arg4)

    /**
     * Runs the block with the given key.
     * @param key Unique key for this invocation.
     */
    operator fun invoke(key: Any?, arg1: A1, arg2: A2, arg3: A3, arg4: A4): T {
        val previous = currentKeyLocal.get()

        return try {
            currentKeyLocal.set(key)
            block(arg1, arg2, arg3, arg4)
        } finally {
            currentKeyLocal.set(previous)
        }
    }
}