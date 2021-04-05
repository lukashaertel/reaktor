package eu.metatools.reaktor.gdx.utils


private val receiver = ThreadLocal<Receiver>()

val currentReceiver
    get(): Receiver? {
        return receiver.get()
    }

@Suppress("non_public_call_from_public_inline")
inline fun <T> runWithReceiver(value: Receiver?, block: () -> T): T {
    val receiverBefore = receiver.get()

    receiver.set(value)
    try {
        return block()
    } finally {
        receiver.set(receiverBefore)
    }
}

/**
 * Consumes items.
 */
interface Receiver {
    /**
     * Receives a single item.
     */
    fun receive(item: Any?)
}

fun <T> T.tryReceive() =
    also {
        currentReceiver?.receive(it)
    }

/**
 * Runs a [generator] for  exactly one item.
 */
inline fun <reified T> generateAtMostOne(generator: () -> Unit): T? {
    // Memorize the generated value.
    var result: T? = null
    var set = false

    // Run generator to collect one value.

    runWithReceiver(object : Receiver {
        override fun receive(item: Any?) {
            if (item !is T)
                throw IllegalArgumentException("Item $item is not of type ${T::class.simpleName}")

            // Require it was not set.
            require(!set) { "Item has already been assigned, current value is $result" }

            // Set and mark as assigned.
            set = true
            result = item
        }
    }, generator)

    // If not assigned, assume null.
    if (!set)
        return null

    // Return.
    @Suppress("unchecked_cast")
    return result as T
}

/**
 * Runs a [generator] for  exactly one item.
 */
inline fun <reified T> generateExactlyOne(generator: () -> Unit): T {
    // Memorize the generated value.
    var result: T? = null
    var set = false

    // Run generator to collect one value.
    runWithReceiver(object : Receiver {
        override fun receive(item: Any?) {
            if (item !is T)
                throw IllegalArgumentException("Item $item is not of type ${T::class.simpleName}")

            // Require it was not set.
            require(!set) { "Item has already been assigned, current value is $result" }

            // Set and mark as assigned.
            set = true
            result = item
        }
    }, generator)

    // Require it was set.
    require(set) { "Item has not been assigned, needs exactly one item." }

    // Return.
    @Suppress("unchecked_cast")
    return result as T
}

/**
 * Runs a [generator] for it's generated items.
 */
inline fun <reified T> generateMany(generator: () -> Unit): List<T> {
    // Generate result store.
    val result = mutableListOf<T>()

    // Run generator to collect.
    runWithReceiver(object : Receiver {
        override fun receive(item: Any?) {
            if (item !is T)
                throw IllegalArgumentException("Item $item is not of type ${T::class.simpleName}")

            result.add(item)
        }
    }, generator)

    // Return the received items.
    return result
}
