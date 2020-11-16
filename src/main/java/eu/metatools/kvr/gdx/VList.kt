package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import eu.metatools.kvr.Delegation
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.internals.extAlign
import eu.metatools.kvr.gdx.internals.extTypeToSelect
import com.badlogic.gdx.scenes.scene2d.ui.List as GdxList

open class VList<T>(
    val style: ListStyle,
    val typeToSelect: Boolean,
    val items: List<T>,
    val selected: T?,
    val align: Int,
    fillParent: Boolean,
    layoutEnabled: Boolean,
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
    ref: Ref<GdxList<T>>? = null
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
        val defaultItems: List<*> = listOf<Any>()
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
        1 -> typeToSelect
        2 -> items
        3 -> selected
        4 -> align
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: GdxList<T>): Any? = when (prop) {
        0 -> actual.style
        1 -> actual.extTypeToSelect
        2 -> Delegation.list(actual, prop,
            { items.size },
            { at -> items.get(at) },
            { at, value: T -> setItems(items.also { it[at] = value }) },
            { value -> setItems(items.also { it.add(value) }) },
            { at ->
                val also = items.also { it.removeIndex(at) }
                setItems(also)
            })
        3 -> actual.selected
        4 -> actual.extAlign
        else -> super.getActual(prop - ownProps, actual)
    }

    override fun updateActual(prop: Int, actual: GdxList<T>, value: Any?) {
        @Suppress("unchecked_cast")
        when (prop) {
            0 -> actual.style = value as ListStyle
            1 -> actual.setTypeToSelect(value as Boolean)
            2 -> throw UnsupportedOperationException()
            3 -> actual.selected = value as T?
            4 -> actual.setAlignment(value as Int)
            else -> super.updateActual(prop - ownProps, actual, value)
        }
    }
}

fun <T> list(
    style: ListStyle,
    typeToSelect: Boolean = VList.defaultTypeToSelect,
    @Suppress("unchecked_cast")
    items: List<T> = VList.defaultItems as List<T>,
    @Suppress("unchecked_cast")
    selected: T? = VList.defaultSelected as T?,
    align: Int = VList.defaultAlign,

    // VWidget
    fillParent: Boolean = VWidget.defaultFillParent,
    layoutEnabled: Boolean = VWidget.defaultLayoutEnabled,

    // VActor
    color: Color = VActor.defaultColor,
    name: String? = VActor.defaultName,
    originX: Float = VActor.defaultOriginX,
    originY: Float = VActor.defaultOriginY,
    x: Float = VActor.defaultX,
    y: Float = VActor.defaultY,
    width: Float = VActor.defaultWidth,
    height: Float = VActor.defaultHeight,
    rotation: Float = VActor.defaultRotation,
    scaleX: Float = VActor.defaultScaleX,
    scaleY: Float = VActor.defaultScaleY,
    visible: Boolean = VActor.defaultVisible,
    debug: Boolean = VActor.defaultDebug,
    touchable: Touchable = VActor.defaultTouchable,
    listeners: List<EventListener> = VActor.defaultListeners,
    captureListeners: List<EventListener> = VActor.defaultCaptureListeners,

    // VRef
    ref: Ref<GdxList<T>>? = null
) = constructTerminal {
    VList(
        style,
        typeToSelect,
        items,
        selected,
        align,
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
    )
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