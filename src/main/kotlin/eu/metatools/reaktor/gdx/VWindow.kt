package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import eu.metatools.reaktor.ex.consumeKey
import eu.metatools.reaktor.gdx.data.ExtentValues
import eu.metatools.reaktor.gdx.internals.extKeepInStage
import eu.metatools.reaktor.gdx.internals.extResizeBorder
import eu.metatools.reaktor.gdx.utils.generateMany
import eu.metatools.reaktor.gdx.utils.tryReceive

@Deprecated("Unsupported:titleTable")
open class VWindow (
    val title: String,
    val style: WindowStyle,
    val modal: Boolean = defaultModal,
    val movable: Boolean = defaultMovable,
    val resizable: Boolean = defaultResizable,
    val resizeBorder: Int = defaultResizeBorder,
    val keepInStage: Boolean = defaultKeepInStage,
    round: Boolean = defaultRound,
    pad: ExtentValues = defaultPad,
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
    ref: (Table) -> Unit = defaultRef,
    key: Any? = consumeKey(),
    children: List<VActor<*>> = defaultChildren,
    cells: List<VCell> = defaultCells,
) : VTable(
    round,
    pad,
    style.background,
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
    key,
    children,
    cells
) {
    companion object {
        const val defaultModal = true
        const val defaultMovable = false
        const val defaultResizable = false
        const val defaultResizeBorder = 8
        const val defaultKeepInStage = true

        private const val ownProps = 7
    }

    override fun create() = Window(title, style)

    override fun assign(actual: Table) {
        actual as Window
        actual.isModal = modal
        actual.isMovable = movable
        actual.isResizable = resizable
        actual.setResizeBorder(resizeBorder)
        actual.setKeepWithinStage(keepInStage)

        super.assign(actual)
    }

    override val props = ownProps + super.props

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> title
        1 -> style
        2 -> modal
        3 -> movable
        4 -> resizable
        5 -> resizeBorder
        6 -> keepInStage
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: Table): Any? {
        actual as Window
        return when (prop) {
            0 -> actual.titleLabel.text
            1 -> actual.style
            2 -> actual.isModal
            3 -> actual.isMovable
            4 -> actual.isResizable
            5 -> actual.extResizeBorder
            6 -> actual.extKeepInStage
            else -> super.getActual(prop - ownProps, actual)
        }
    }

    override fun updateActual(prop: Int, actual: Table, value: Any?) {
        actual as Window
        when (prop) {
            0 -> actual.titleLabel.setText(value as String)
            1 -> actual.style = value as WindowStyle
            2 -> actual.isModal = value as Boolean
            3 -> actual.isMovable = value as Boolean
            4 -> actual.isResizable = value as Boolean
            5 -> actual.setResizeBorder(value as Int)
            6 -> actual.setKeepWithinStage(keepInStage)
            else -> super.updateActual(prop - ownProps, actual, value)
        }
    }
}

inline fun window(
    title: String,
    style: WindowStyle,
    modal: Boolean = VWindow.defaultModal,
    movable: Boolean = VWindow.defaultMovable,
    resizable: Boolean = VWindow.defaultResizable,
    resizeBorder: Int = VWindow.defaultResizeBorder,
    keepInStage: Boolean = VWindow.defaultKeepInStage,
    round: Boolean = VTable.defaultRound,
    pad: ExtentValues = VTable.defaultPad,
    fillParent: Boolean = VWidgetGroup.defaultFillParent,
    layoutEnabled: Boolean = VWidgetGroup.defaultLayoutEnabled,
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
    touchable: Touchable = VTable.defaultTouchable,
    listeners: List<EventListener> = VActor.defaultListeners,
    captureListeners: List<EventListener> = VActor.defaultCaptureListeners,
    noinline ref: (Table) -> Unit = VRef.defaultRef,
    key: Any? = consumeKey(),
    generateChildren: ()->Unit = {},
    generateCells: ()->Unit = {},
): VWindow {
    val children = generateMany<VActor<*>>(generateChildren)
    val cells = generateMany<VCell>(generateCells)
    return VWindow(title,
        style,
        modal,
        movable,
        resizable,
        resizeBorder,
        keepInStage,
        round,
        pad,
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
        key,
        children,
        cells).tryReceive()
}