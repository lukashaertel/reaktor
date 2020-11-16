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
import eu.metatools.kvr.gdx.utils.hidden
import com.badlogic.gdx.scenes.scene2d.ui.List as GdxList

data class VList<T>(
    // VList
    val style: ListStyle,
    val typeToSelect: Boolean,
    val items: List<T>,
    val selected: T?,
    val align: Int,

    // VWidget
    override val fillParent: Boolean,
    override val layoutEnabled: Boolean,

    // VActor
    override val color: Color,
    override val name: String?,
    override val originX: Float,
    override val originY: Float,
    override val x: Float,
    override val y: Float,
    override val width: Float,
    override val height: Float,
    override val rotation: Float,
    override val scaleX: Float,
    override val scaleY: Float,
    override val visible: Boolean,
    override val debug: Boolean,
    override val touchable: Touchable,
    override val listeners: List<EventListener>,
    override val captureListeners: List<EventListener>,

    // VRef
    override val ref: Ref<GdxList<T>>? = null
) : VWidget<GdxList<T>>() {
    companion object {
        val defaultTypeToSelect: Boolean = false
        val defaultItems: List<*> = listOf<Any>()
        val defaultSelected: Any? = null
        val defaultAlign: Int = Align.left
        private val align = hidden<GdxList<*>, Int>("alignment")

        private val typeToSelect = hidden<GdxList<*>, Boolean>("typeToSelect")
    }

    override fun create() = GdxList<T>(style)

    override fun assign(actual: GdxList<T>) {
        actual.setTypeToSelect(typeToSelect)
        actual.setItems(actual.items.also { items.forEach(it::add) })
        actual.selected = selected
        actual.setAlignment(align)

        super.assign(actual)
    }

    override fun props() = 23

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> style
        1 -> typeToSelect
        2 -> items
        3 -> selected
        4 -> align
        5 -> fillParent
        6 -> layoutEnabled
        7 -> color
        8 -> name
        9 -> originX
        10 -> originY
        11 -> x
        12 -> y
        13 -> width
        14 -> height
        15 -> rotation
        16 -> scaleX
        17 -> scaleY
        18 -> visible
        19 -> debug
        20 -> debug
        21 -> listeners
        22 -> captureListeners
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: GdxList<T>): Any? = when (prop) {
        0 -> actual.style
        1 -> typeToSelect(actual)
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
        4 -> align(actual)
        5 -> fillParent(actual)
        6 -> layoutEnabled(actual)
        7 -> actual.color
        8 -> actual.name
        9 -> actual.originX
        10 -> actual.originY
        11 -> actual.x
        12 -> actual.y
        13 -> actual.width
        14 -> actual.height
        15 -> actual.rotation
        16 -> actual.scaleX
        17 -> actual.scaleY
        18 -> actual.isVisible
        19 -> actual.debug
        20 -> actual.touchable
        21 -> wrapListeners(prop, actual.listeners)
        22 -> wrapListeners(prop, actual.captureListeners)
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: GdxList<T>, value: Any?) {
        @Suppress("unchecked_cast")
        when (prop) {
            0 -> actual.style = value as ListStyle
            1 -> actual.setTypeToSelect(value as Boolean)
            2 -> throw UnsupportedOperationException()
            3 -> actual.selected = value as T?
            4 -> actual.setAlignment(value as Int)

            5 -> actual.setFillParent(value as Boolean)
            6 -> actual.setLayoutEnabled(value as Boolean)
            7 -> actual.color = value as Color
            8 -> actual.name = value as String?
            9 -> actual.originX = value as Float
            10 -> actual.originY = value as Float
            11 -> actual.x = value as Float
            12 -> actual.y = value as Float
            13 -> actual.width = value as Float
            14 -> actual.height = value as Float
            15 -> actual.rotation = value as Float
            16 -> actual.scaleX = value as Float
            17 -> actual.scaleY = value as Float
            18 -> actual.isVisible = value as Boolean
            19 -> actual.debug = value as Boolean
            20 -> actual.touchable = value as Touchable
            21 -> throw UnsupportedOperationException()
            22 -> throw UnsupportedOperationException()
            else -> throw IndexOutOfBoundsException(prop)
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