package eu.metatools.reaktor.gdx

import eu.metatools.reaktor.Virtual
import eu.metatools.reaktor.ex.consumeKey

/**
 * Base class for virtual nodes.
 */
abstract class VBase<A : Any>(
    override val key: Any? = consumeKey(),
) : Virtual<A>() {
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