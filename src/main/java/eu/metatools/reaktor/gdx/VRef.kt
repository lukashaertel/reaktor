package eu.metatools.reaktor.gdx

import eu.metatools.reaktor.gdx.data.Ref

/**
 * Forward reference provider.
 * @property ref Not a prop, to push out references.
 */
abstract class VRef<A : Any>(
    val ref: Ref?
) : VBase<A>() {
    companion object {
        val defaultRef: Ref? = null
    }

    override fun begin(actual: A) {
    }

    override fun end(actual: A) {
        // Assign the reference if given.
        ref?.current = actual
    }
}