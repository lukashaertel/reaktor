package eu.metatools.kvr

fun release(v: Any?, actual: Any?) {
    // If v is virtual, use the provided release method.
    if (v is Virtual<*>) {
        @Suppress("unchecked_cast")
        v as Virtual<Any>
        return v.release(actual as Any)
    }

    if (v is AutoCloseable)
        return v.close()
}