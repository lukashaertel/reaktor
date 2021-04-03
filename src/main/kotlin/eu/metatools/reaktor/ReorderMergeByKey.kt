package eu.metatools.reaktor

/**
 * Reorders, adds elements to, and removes elements from the [actual] list based on [source] to [target] transition.
 * Matching is performed based on [match] on two elements. If no element in [actual] is present for [target], a new
 * element is created by [make]. If a correspondence has been found, [merge] combines the elements.
 * @param source The source virtual list.
 * @param target The target virtual list.
 * @param actual The actual list.
 * @param match Returns true if the arguments match.
 * @param make Creates the actual representation of the virtual element.
 * @param merge Merges a source and target virtual with regard to the actual element.
 * @throws IllegalStateException If a key has already been consumed, an exception is thrown.
 * @param V The type of the virtual elements.
 * @param A The type of the actual elements.
 *
 */
inline fun <V, A> reorderMergeByKey(
    source: List<V>, target: List<V>, actual: MutableList<A>,
    match: (unmoved: Boolean, a: V, b: V) -> Boolean,
    make: (virtual: V) -> A, release: (virtual: V, actual: A) -> Unit, merge: (from: V, to: V, on: A) -> A,
) {
    // Create co-running mutable list.
    val tracker = source.toMutableList()
    val illegal = MutableList(tracker.size) { false }

    // Assert to be list that can hold null. We want to add placeholder empty elements for index purposes but do not
    // want them to have actual semantics.
    @Suppress("unchecked_cast")
    tracker as MutableList<Any?>

    // Iterate target with index.
    for ((i, to) in target.withIndex()) {
        // Find source location. Ignore placeholder slots. // TODO: Better key indexing and co-shuffling.
        val src = tracker.withIndex().indexOfFirst { (j, c) -> match(i == j, to, c) }
        if (0 <= src) {
            // Remove original values.
            val from = tracker.removeAt(src)
            val used = illegal.removeAt(src)

            // If used, throw an error here.
            if (used)
                throw IllegalStateException("The element $to at $i was already matched.")

            // Add at the correct position.
            tracker.add(i, from)
            illegal.add(i, true)
            actual.add(i, merge(from, to, actual.removeAt(src)))
        } else {
            // Add an empty mark and add new element.
            tracker.add(i, null)
            illegal.add(i, false)
            actual.add(i, make(to))
        }
    }

    // Trim excess.
    for (i in tracker.size.dec() downTo target.size) {
        release(tracker.removeAt(i), actual.removeAt(i))
    }
}