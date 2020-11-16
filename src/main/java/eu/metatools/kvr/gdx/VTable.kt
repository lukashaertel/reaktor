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

data class VTable(
    val cells: List<VCell>,
    val round: Boolean,
    val pad: ExtentValues,
    val background: Drawable?,

    // VWidgetGroup
    override val fillParent: Boolean,
    override val layoutEnabled: Boolean,

    // VGroup
    override val children: List<VActor<*>>,

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
    override val ref: Ref<Table>? = null
) : VWidgetGroup<Table>() {
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

    override fun props() = 23

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> orderedCells
        1 -> round
        2 -> pad
        3 -> background
        4 -> fillParent
        5 -> layoutEnabled
        6 -> children
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
        20 -> touchable
        21 -> listeners
        22 -> captureListeners
        else -> throw IndexOutOfBoundsException(prop)
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
        4 -> fillParent(actual)
        5 -> layoutEnabled(actual)
        6 -> wrapChildren(prop, actual)
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

    override fun updateActual(prop: Int, actual: Table, value: Any?) {
        when (prop) {
            0 -> throw UnsupportedOperationException()
            1 -> actual.setRound(value as Boolean)
            2 -> actual.pad((value as ExtentValues).top, value.left, value.bottom, value.right)
            3 -> actual.background = value as Drawable?
            4 -> actual.setFillParent(value as Boolean)
            5 -> actual.setLayoutEnabled(value as Boolean)
            6 -> updateActualChildren()
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