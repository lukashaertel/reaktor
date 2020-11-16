package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Touchable
import eu.metatools.kvr.Delegation
import eu.metatools.kvr.gdx.data.Ref

abstract class VGroup<A : Group>(
    val children: List<VActor<*>>,
    color: Color,
    name: String?,
    originX: Float,
    originY: Float,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    rotation: Float,
    scaleX: Float,
    scaleY: Float,
    visible: Boolean,
    debug: Boolean,
    touchable: Touchable,
    listeners: List<EventListener>,
    captureListeners: List<EventListener>,
    ref: Ref<A>?
) : VActor<A>(color,
    name,
    originX,
    originY,
    x,
    y,
    width,
    height,
    rotation,
    scaleX,
    scaleY,
    visible,
    debug,
    touchable,
    listeners,
    captureListeners,
    ref) {
    companion object {
        val defaultChildren = listOf<VActor<*>>()

        private const val ownProps = 1
    }

    override fun assign(actual: A) {
        for (child in children)
            actual.addActor(child.make())

        super.assign(actual)
    }

    override val props = ownProps + super.props

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> children
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: A): Any? = when (prop) {
        0 -> wrapChildren(prop, actual)
        else -> super.getActual(prop - ownProps, actual)
    }

    override fun updateActual(prop: Int, actual: A, value: Any?) {
        when (prop) {
            0 -> throw UnsupportedOperationException()
            else -> super.updateActual(prop - ownProps, actual, value)
        }
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