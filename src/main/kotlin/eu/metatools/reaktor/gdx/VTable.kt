package eu.metatools.reaktor.gdx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import eu.metatools.reaktor.Delegation
import eu.metatools.reaktor.ex.consumeKey
import eu.metatools.reaktor.gdx.data.ExtentValues
import eu.metatools.reaktor.gdx.internals.*
import eu.metatools.reaktor.gdx.utils.generateMany
import eu.metatools.reaktor.gdx.utils.tryReceive

open class VTable (
    val round: Boolean = defaultRound,
    val pad: ExtentValues = defaultPad,
    val background: Drawable? = defaultBackground,
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
    val cells: List<VCell> = defaultCells,
) : VWidgetGroup<Table>(
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
    children
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
        val defaultCells = listOf<VCell>()


        private const val ownProps = 4

        /**
         * Cells and their actors before update.
         */
        private val beforeRecon = ThreadLocal.withInitial { HashSet<Pair<Cell<Actor>, Actor?>>() }

    }

    private val correctedCells = run {
        // Get width.
        val max = cells.asSequence().map { it.column + it.colSpan }.maxOrNull()?.inc()

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
        for (cell in correctedCells) {
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
        0 -> correctedCells
        1 -> round
        2 -> pad
        3 -> background
        4 -> fillParent
        else -> super.getOwn(prop - ownProps)
    }

    override fun getActual(prop: Int, actual: Table): Any? = when (prop) {
        0 -> Delegation.list(actual, prop,
            size = {
                cells.size
            },
            get = { at ->
                cells[at]
            },
            set = { at, value: Cell<Actor> ->
                // Get previous value, set new value, return previous.
                val result = cells[at]
                cells[at] = value
                result
            },
            add = { value ->
                // Add value and return if last is the added value.
                cells.add(value)
                cells.last() === value
            },
            addAt = { at, value: Cell<Actor> ->
                // Insert the cell, update items.
                cells.insert(at, value)
            },
            removeAt = { at ->
                // Remove cell.
                cells.removeIndex(at)
            }
        )
        1 -> actual.extRound
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

        // Table has issues with insertion, invalidate layout after updates.
        actual.invalidate()

        super.end(actual)
    }

    /**
     * Updates above indices and end-ness for all [Table.cells] of a [Table]. Sets the table's column and
     * row counts.
     */
    private fun updateCells(actual: Table) {
        // Paint the index of the cell for all locations, including column spans.
        val indexFromLocation = actual.cells.withIndex().flatMap { (index, cell) ->
            (0 until cell.colspan).map { oc ->
                (cell.column + oc to cell.row) to index
            }
        }.associate { it }

        // Set last to each highest cell in a row.
        actual.cells.groupBy { it.row }.forEach { (_, row) ->
            val last = row.maxByOrNull { it.column }!!
            last.extEndRow = true
            for (other in row)
                if (other !== last)
                    other.extEndRow = false
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
                cell.extCellAboveIndex = it ?: -1
            }
        }

        // Set columns and rows count for table.
        actual.extColumns = maxColumn.inc()
        actual.extRows = maxRow.inc()
    }
}

inline fun table(
    round: Boolean = VTable.defaultRound,
    pad: ExtentValues = VTable.defaultPad,
    background: Drawable? = VTable.defaultBackground,
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
): VTable {
    val children = generateMany<VActor<*>>(generateChildren)
    val cells = generateMany<VCell>(generateCells)
    return VTable(round,
        pad,
        background,
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