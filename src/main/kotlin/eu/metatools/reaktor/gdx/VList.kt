package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import eu.metatools.reaktor.Delegation
import eu.metatools.reaktor.gdx.data.Ref
import eu.metatools.reaktor.gdx.internals.extAlign
import eu.metatools.reaktor.gdx.internals.extTypeToSelect
import com.badlogic.gdx.scenes.scene2d.ui.List as GdxList

open class VList<T>(
    val style: ListStyle,
    val items: List<T>,
    val typeToSelect: Boolean = defaultTypeToSelect,
    val selected: T? = defaultSelected as T?,
    val align: Int = defaultAlign,
    fillParent: Boolean = defaultFillParent,
    layoutEnabled: Boolean = defaultLayoutEnabled,
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
    ref: Ref? = defaultRef,
) : VWidget<GdxList<T>>(
    fillParent,
    layoutEnabled,
    color,
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
    ref
) {
    companion object {
        val defaultTypeToSelect: Boolean = false
        val defaultSelected: Any? = null
        val defaultAlign: Int = Align.left
        private const val ownProps = 5
    }

    override fun create() = GdxList<T>(style)

    override fun assign(actual: GdxList<T>) {
        actual.setTypeToSelect(typeToSelect)
        actual.setItems(actual.items.also { items.forEach(it::add) })
        actual.selected = selected
        actual.setAlignment(align)

        super.assign(actual)
    }

    override val props = ownProps + super.props

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> style
        1 -> items
        2 -> typeToSelect
        3 -> selected
        4 -> align
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: GdxList<T>): Any? = when (prop) {
        0 -> actual.style
        1 -> Delegation.list(actual, prop,
            { items.size },
            { at -> items.get(at) },
            { at, value: T -> setItems(items.also { it[at] = value }) },
            { value -> setItems(items.also { it.add(value) }) },
            { at ->
                val also = items.also { it.removeIndex(at) }
                setItems(also)
            })
        2 -> actual.extTypeToSelect
        3 -> actual.selected
        4 -> actual.extAlign
        else -> super.getActual(prop - ownProps, actual)
    }

    override fun updateActual(prop: Int, actual: GdxList<T>, value: Any?) {
        @Suppress("unchecked_cast")
        when (prop) {
            0 -> actual.style = value as ListStyle
            1 -> throw UnsupportedOperationException()
            2 -> actual.setTypeToSelect(value as Boolean)
            3 -> actual.selected = value as T?
            4 -> actual.setAlignment(value as Int)
            else -> super.updateActual(prop - ownProps, actual, value)
        }
    }
}

/**
 * For a change event from a [GdxList], returns the selected item.
 */
val ChangeListener.ChangeEvent.listSelected: Any? get() = (target as GdxList<*>).selected

/**
 * For a change event from a [GdxList], returns the selected items.
 */
val ChangeListener.ChangeEvent.listSelection: ArraySelection<out Any> get() = (target as GdxList<*>).selection

/**
 * For a change event from a [GdxList], returns the selected item index.
 */
val ChangeListener.ChangeEvent.listSelectedIndex get() = (target as GdxList<*>).selectedIndex