package eu.metatools.kvr.gdx

import eu.metatools.kvr.gdx.data.Ref

/**
 * Forward reference provider.
 * @property ref Not a prop, to push out references.
 */
abstract class VRef<A : Any>(
    val ref: Ref<A>?
) : VBase<A>() {
    companion object {
        val defaultRef: Any? = null
    }

    override fun begin(actual: A) {
    }

    override fun end(actual: A) {
        // Assign the reference if given.
        ref?.current = actual
    }
}