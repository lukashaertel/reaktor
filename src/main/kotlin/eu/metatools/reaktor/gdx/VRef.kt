package eu.metatools.reaktor.gdx

import eu.metatools.reaktor.ex.consumeKey

/**
 * Forward reference provider.
 * @property ref Not a prop, to push out references.
 */
abstract class VRef<A : Any>(
    val ref: (A) -> Unit = defaultRef,
    key: Any? = consumeKey(),
) : VBase<A>(key) {
    companion object {
        val defaultRef: (Any?) -> Unit = {}
    }

    override fun make() =
        super.make().also(ref)

    override fun begin(actual: A) {
    }

    override fun end(actual: A) {
        // Assign the reference if given.
        ref(actual)
    }
}