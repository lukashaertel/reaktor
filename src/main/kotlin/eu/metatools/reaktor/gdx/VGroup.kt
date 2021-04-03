package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Touchable
import eu.metatools.reaktor.Delegation
import eu.metatools.reaktor.ex.consumeKey

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
    key: Any? = consumeKey(),
    init: Receiver<VActor<*>> = {},
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
    ref,
    key) {
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
        size = {
            // Return the size of the current children.
            children.size
        },
        get = { at ->
            // Get child at.
            getChild(at)
        },
        set = { at, value: Actor ->
            // Get original value.
            val overwritten = getChild(at)

            // Overwrite.
            removeActor(overwritten)
            addActorAt(at, value)

            // If not added, there's something going wrong.
            if (value.parent != this)
                throw IndexOutOfBoundsException(at)

            // Return overwritten element.
            overwritten
        },
        add = { value: Actor ->
            // Add actor and check that it was added.
            addActor(value)
            value.parent == this
        },
        addAt = { at, value: Actor ->
            // Add actor at index.
            addActorAt(at, value)
        },
        removeAt = { at ->
            // Get item to remove. Remove it and return it.
            val removed = children[at]
            removeActor(removed)
            removed
        }
    )

fun updateActualChildren() {
    throw UnsupportedOperationException()
}