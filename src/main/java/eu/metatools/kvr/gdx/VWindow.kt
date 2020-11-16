package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import eu.metatools.kvr.Delegation
import eu.metatools.kvr.gdx.data.ExtentValues
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.utils.hidden

// TODO: Derive from table.

@Deprecated("Unsupported:titleTable")
open class VWindow(
    val title: String,
    val style: WindowStyle,
    val modal: Boolean,
    val movable: Boolean,
    val resizable: Boolean,
    val resizeBorder: Int,
    val keepInStage: Boolean,
    val cells: List<VCell>,
    val round: Boolean,
    val pad: ExtentValues,
    fillParent: Boolean,
    layoutEnabled: Boolean,
    children: List<VActor<*>>,
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
    ref: Ref<Window>?
) : VWidgetGroup<Window>(
    fillParent,
    layoutEnabled,
    children,
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
        const val defaultModal = true
        const val defaultMovable = false
        const val defaultResizable = false
        const val defaultResizeBorder = 8
        const val defaultKeepInStage = true
        const val defaultRound = true
        val defaultPad = ExtentValues(
            Table.backgroundTop,
            Table.backgroundLeft,
            Table.backgroundBottom,
            Table.backgroundRight)

        private val round = hidden<Table, Boolean>("round")
        private val resizeBorder = hidden<Window, Int>("resizeBorder")
        private val keepInStage = hidden<Window, Int>("keepWithinStage")

        private const val ownProps = 10

        /**
         * Cells and their actors before update.
         */
        private val beforeRecon = ThreadLocal.withInitial { HashSet<Pair<Cell<Actor>, Actor?>>() }
    }

    private val orderedCells = run {
        // Get width.
        val max = cells.asSequence().map { it.column + it.colSpan }.max()?.inc()

        // No width means no cells, otherwise sort row major/column minor.
        if (max == null)
            cells
        else
            cells.sortedBy { it.row * max + it.column }
    }

    override fun create() = Window(title, style)

    override fun assign(actual: Window) {
        actual.isModal = modal
        actual.isMovable = movable
        actual.isResizable = resizable
        actual.setResizeBorder(resizeBorder)
        actual.setKeepWithinStage(keepInStage)
        actual.setRound(round)
        actual.pad(pad.top, pad.left, pad.bottom, pad.right)

        // For all cells in LTR-TTB order, make cells and add.
        for (cell in orderedCells) {
            // Make cell and set table.
            val actualCell = cell.make()
            actualCell.table = actual

            // Add cell.
            actual.cells.add(actualCell)

            // If actor is present, add it to the table.
            if (actualCell.actor != null)
                actual.addActor(actualCell.actor)
        }

        // Update all cells.
        VTable.updateCells(actual)

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
        7 -> orderedCells
        8 -> round
        9 -> pad
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: Window): Any? = when (prop) {
        0 -> actual.titleLabel.text
        1 -> actual.style
        2 -> actual.isModal
        3 -> actual.isMovable
        4 -> actual.isResizable
        5 -> resizeBorder(actual)
        6 -> keepInStage(actual)
        7 -> Delegation.list(actual, prop,
            { cells.size },
            { at -> cells.get(at) },
            { at, value: Cell<Actor> ->
                cells.set(at, value)
            },
            { value -> cells.add(value) },
            { at -> cells.removeIndex(at) }
        )
        8 -> round(actual)
        9 -> ExtentValues(actual.padTop, actual.padLeft, actual.padBottom, actual.padRight)
        else -> super.getActual(prop - ownProps, actual)
    }

    override fun updateActual(prop: Int, actual: Window, value: Any?) {
        when (prop) {
            0 -> actual.titleLabel.setText(value as String)
            1 -> actual.style = value as WindowStyle
            2 -> actual.isModal = value as Boolean
            3 -> actual.isMovable = value as Boolean
            4 -> actual.isResizable = value as Boolean
            5 -> actual.setResizeBorder(value as Int)
            6 -> actual.setKeepWithinStage(keepInStage)
            7 -> throw UnsupportedOperationException()
            8 -> actual.setRound(value as Boolean)
            9 -> actual.pad((value as ExtentValues).top, value.left, value.bottom, value.right)
            else -> super.updateActual(prop - ownProps, actual, value)
        }
    }

    override fun begin(actual: Window) {
        beforeRecon.get().also {
            it.clear()
            actual.cells.forEach { cell ->
                it.add(cell to cell.actor)
            }
        }

        super.begin(actual)
    }

    override fun end(actual: Window) {
        // Self diff for actor add and removal.
        val before = beforeRecon.get()
        val after = actual.cells.mapTo(mutableSetOf()) { it to it.actor }

        // Apply diff.
        for ((cell, actor) in before subtract after) {
            cell.table = null
            if (actor != null)
                actual.removeActor(actor)
        }
        for ((cell, actor) in after subtract before) {
            cell.table = actual
            if (actor != null)
                actual.addActor(actor)
        }

        // Compute cell locations, indices and counts.
        VTable.updateCells(actual)

        super.end(actual)
    }
}

inline fun window(
    // VWindow
    title: String,
    style: WindowStyle,
    modal: Boolean = VWindow.defaultModal,
    movable: Boolean = VWindow.defaultMovable,
    resizable: Boolean = VWindow.defaultResizable,
    resizeBorder: Int = VWindow.defaultResizeBorder,
    keepInStage: Boolean = VWindow.defaultKeepInStage,
    round: Boolean = VWindow.defaultRound,
    pad: ExtentValues = VWindow.defaultPad,

    // VWidgetGroup
    fillParent: Boolean = VWidgetGroup.defaultFillParent,
    layoutEnabled: Boolean = VWidgetGroup.defaultLayoutEnabled,

    // VGroup
    children: List<VActor<*>> = VGroup.defaultChildren,

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
    @Suppress("unchecked_cast")
    ref: Ref<Window>? = VRef.defaultRef as Ref<Window>?,

    // VWindow
    cells: () -> Unit
) = constructParent<VCell, VWindow>(cells) {
    VWindow(
        title,
        style,
        modal,
        movable,
        resizable,
        resizeBorder,
        keepInStage,
        it,
        round,
        pad,
        fillParent,
        layoutEnabled,
        children,
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