package eu.metatools.reaktor.gdx

/**
 * Forward reference provider.
 * @property ref Not a prop, to push out references.
 */
abstract class VRef<A : Any>(
    val ref: (A) -> Unit = defaultRef,
) : VBase<A>() {
    companion object {
        val defaultRef: (Any?) -> Unit = {}
    }

    override fun begin(actual: A) {
    }

    override fun end(actual: A) {
        // Assign the reference if given.
        ref(actual)
    }
}