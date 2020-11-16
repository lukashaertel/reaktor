package eu.metatools.kvr.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import eu.metatools.kvr.Delegation
import eu.metatools.kvr.gdx.data.ExtentValues
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.utils.hidden

open class VTable(
    val cells: List<VCell>,
    val round: Boolean,
    val pad: ExtentValues,
    val background: Drawable?,
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
    ref: Ref<Table>? = null
) : VWidgetGroup<Table>(
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
        val defaultRound = true
        val defaultPad = ExtentValues(
            Table.backgroundTop,
            Table.backgroundLeft,
            Table.backgroundBottom,
            Table.backgroundRight)
        val defaultBackground = null
        val defaultTouchable = Touchable.childrenOnly

        private val round = hidden<Table, Boolean>("round")
        private val columns = hidden<Table, Int>("columns")
        private val rows = hidden<Table, Int>("rows")

        private const val ownProps = 4

        /**
         * Cells and their actors before update.
         */
        private val beforeRecon = ThreadLocal.withInitial { HashSet<Pair<Cell<Actor>, Actor?>>() }

        /**
         * Updates above indices and end-ness for all [Table.cells] of a [Table]. Sets the table's column and
         * row counts.
         */
        fun updateCells(actual: Table) {
            // Paint the index of the cell for all locations, including column spans.
            val indexFromLocation = actual.cells.withIndex().flatMap { (index, cell) ->
                (0 until cell.colspan).map { oc ->
                    (cell.column + oc to cell.row) to index
                }
            }.associate { it }

            // Set last to each highest cell in a row.
            actual.cells.groupBy { it.row }.forEach { (_, row) ->
                val last = row.maxBy { it.column }!!
                VCell.endRow(last, true)
                for (other in row)
                    if (other !== last)
                        VCell.endRow(other, false)
            }

            // Highest seen row and column.
            var maxColumn = 0
            var maxRow = 0

            // Visit rows and columns and assign above index.
            actual.cells.forEach { cell ->
                // Max aggregate.
                maxColumn = maxOf(maxColumn, cell.column + cell.colspan)
                maxRow = maxOf(maxRow, cell.row)

                // Set index above or minus one.
                indexFromLocation[cell.column to cell.row.dec()].let {
                    VCell.cellAboveIndex(cell, it ?: -1)
                }
            }

            // Set columns and rows count for table.
            columns(actual, maxColumn.inc())
            rows(actual, maxRow.inc())
        }
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

    override fun create() = Table()

    override fun assign(actual: Table) {
        actual.setRound(round)
        actual.pad(pad.top, pad.left, pad.bottom, pad.right)
        actual.background = background

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
        updateCells(actual)

        super.assign(actual)
    }

    override val props = ownProps + super.props

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> orderedCells
        1 -> round
        2 -> pad
        3 -> background
        4 -> fillParent
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: Table): Any? = when (prop) {
        0 -> Delegation.list(actual, prop,
            { cells.size },
            { at -> cells.get(at) },
            { at, value: Cell<Actor> ->
                cells.set(at, value)
            },
            { value -> cells.add(value) },
            { at -> cells.removeIndex(at) }
        )
        1 -> round(actual)
        2 -> ExtentValues(actual.padTop, actual.padLeft, actual.padBottom, actual.padRight)
        3 -> actual.background
        else -> super.getActual(prop - ownProps, actual)
    }

    override fun updateActual(prop: Int, actual: Table, value: Any?) {
        when (prop) {
            0 -> throw UnsupportedOperationException()
            1 -> actual.setRound(value as Boolean)
            2 -> actual.pad((value as ExtentValues).top, value.left, value.bottom, value.right)
            3 -> actual.background = value as Drawable?
            else -> super.updateActual(prop - ownProps, actual, value)
        }
    }

    override fun begin(actual: Table) {
        beforeRecon.get().also {
            it.clear()
            actual.cells.forEach { cell ->
                it.add(cell to cell.actor)
            }
        }

        super.begin(actual)
    }

    override fun end(actual: Table) {
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
        updateCells(actual)

        super.end(actual)
    }
}

inline fun table(
    // VTable
    round: Boolean = VTable.defaultRound,
    pad: ExtentValues = VTable.defaultPad,
    background: Drawable? = VTable.defaultBackground,

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
    touchable: Touchable = VTable.defaultTouchable,
    listeners: List<EventListener> = VActor.defaultListeners,
    captureListeners: List<EventListener> = VActor.defaultCaptureListeners,

    // VRef
    @Suppress("unchecked_cast")
    ref: Ref<Table>? = VRef.defaultRef as Ref<Table>?,

    // VTable
    cells: () -> Unit
) = constructParent<VCell, VTable>(cells) {
    VTable(
        it,
        round,
        pad,
        background,
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