package eu.metatools.kvr.gdx

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import eu.metatools.kvr.Delegation

abstract class VGroup<A : Group> : VActor<A>() {
    companion object {
        val defaultChildren = listOf<VActor<*>>()
    }

    abstract val children: List<VActor<*>>

    override fun assign(actual: A) {
        for (child in children)
            actual.addActor(child.make())

        super.assign(actual)
    }
}

fun wrapChildren(prop: Int, actual: Group) =
    Delegation.list(actual, prop,
        { children.size },
        { at -> getChild(at) },
        { at, value: Actor -> addActorAt(at, value); removeActor(children[at.inc()]) },
        { value -> addActor(value) },
        { at -> removeActor(children[at]) }
    )

fun updateActualChildren() {
    throw UnsupportedOperationException()
}