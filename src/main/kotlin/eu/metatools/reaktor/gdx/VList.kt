package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import eu.metatools.reaktor.Delegation
import eu.metatools.reaktor.ex.consumeKey
import eu.metatools.reaktor.gdx.internals.extAlign
import eu.metatools.reaktor.gdx.internals.extTypeToSelect
import eu.metatools.reaktor.gdx.utils.tryReceive
import com.badlogic.gdx.scenes.scene2d.ui.List as GdxList

open class VList<T> (
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
    ref: (GdxList<T>) -> Unit = defaultRef,
    key: Any? = consumeKey(),
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
    ref,
    key
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

    override fun begin(actual: com.badlogic.gdx.scenes.scene2d.ui.List<T>) {
        super.begin(actual)
    }

    override fun end(actual: com.badlogic.gdx.scenes.scene2d.ui.List<T>) {
        super.end(actual)
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
            size = {
                // Return the item's size.
                items.size
            },
            get = { at ->
                // Get the item at the position.
                items.get(at)
            },
            set = { at, value: T ->
                // Get previous value, set new value, update items, return previous.
                val result = items[at]
                items[at] = value
                setItems(items)
                result
            },
            add = { value: T ->
                // Add value, update items and return if last is the added value.
                items.add(value)
                setItems(items)
                items.last() === value
            },
            addAt = { at, value: T ->
                // Insert the item, update items.
                items.insert(at, value)
                setItems(items)
            },
            removeAt = { at ->
                // Remove the item, update items and return the removed item.
                val result = items.removeIndex(at)
                setItems(items)
                result
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

inline fun <T> list(
    style: ListStyle,
    items: List<T>,
    typeToSelect: Boolean = VList.defaultTypeToSelect,
    selected: T? = VList.defaultSelected as T?,
    align: Int = VList.defaultAlign,
    fillParent: Boolean = VWidget.defaultFillParent,
    layoutEnabled: Boolean = VWidget.defaultLayoutEnabled,
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
   noinline  ref: (GdxList<T>) -> Unit = VRef.defaultRef,
    key: Any? = consumeKey(),
): VList<T> {
    return VList(style,
        items,
        typeToSelect,
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
        ref,
        key).tryReceive()
}