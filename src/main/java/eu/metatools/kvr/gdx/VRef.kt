package eu.metatools.kvr.gdx

import eu.metatools.kvr.gdx.data.Ref

/**
 * Forward reference provider.
 */
abstract class VRef<A : Any> : VBase<A>() {
    companion object {
        val defaultRef: Any? = null
    }

    /**
     * Not a prop, to push out references.
     */
    abstract val ref: Ref<A>?

    override fun begin(actual: A) {
    }

    override fun end(actual: A) {
        // Assign the reference if given.
        ref?.current = actual
    }
}