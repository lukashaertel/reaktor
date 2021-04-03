package eu.metatools.reaktor.util

/**
 * Returns the [n]th instance of [T] in the receiver.
 */
inline fun <reified T> Iterable<*>.nthIsInstance(n: Int) =
    asSequence().filterIsInstance<T>().elementAt(n)