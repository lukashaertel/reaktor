package eu.metatools.kvr.gdx

import eu.metatools.kvr.Virtual

/**
 * Base class for virtual nodes.
 */
abstract class VBase<A : Any> : Virtual<A> {
    override fun make() = create().also(::assign)

    /**
     * Creates the instance of [A] with only the required constructor arguments.
     */
    protected abstract fun create(): A

    /**
     * Assigns the non-constructor arguments.
     */
    protected open fun assign(actual: A) = Unit
}