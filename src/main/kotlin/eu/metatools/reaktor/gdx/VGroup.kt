package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import eu.metatools.reaktor.Delegation

abstract class VGroup<A : Group>(
    color: Color = defaultColor,
    name: String? = defaultName,
    originX: Float = defaultOriginX,
    originY: Float = defaultOriginY,
    x: Float = defaultX,
    y: Float = defaultY,
    width: Float = defaultWidth,
    height: Float = defaultHeight,
    rotation: Float = defaultRotation,
    scaleX: Float = defaultScaleX,
    scaleY: Float = defaultScaleY,
    visible: Boolean = defaultVisible,
    debug: Boolean = defaultDebug,
    touchable: Touchable = defaultTouchable,
    listeners: List<EventListener> = defaultListeners,
    captureListeners: List<EventListener> = defaultCaptureListeners,
    ref: (A) -> Unit = defaultRef,
    init: Receiver<VActor<*>> = {}
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

    val children: List<VActor<*>> = mutableListOf()

    init {
        children as MutableList
        init(ReceiveMany { children.add(it) })
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