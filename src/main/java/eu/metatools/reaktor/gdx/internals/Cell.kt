package eu.metatools.reaktor.gdx.internals

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import eu.metatools.reaktor.gdx.utils.hidden


private val cellActor = hidden<Cell<*>, Actor?>("actor")
private val cellEndRow = hidden<Cell<*>, Boolean>("endRow")
private val cellCellAboveIndex = hidden<Cell<*>, Int>("cellAboveIndex")
private val cellColumn = hidden<Cell<*>, Int>("column")
private val cellRow = hidden<Cell<*>, Int>("row")

var Cell<*>.extActor
    get() = cellActor(this)
    set(value) = cellActor(this, value)

var Cell<*>.extEndRow
    get() = cellEndRow(this)
    set(value) = cellEndRow(this, value)

var Cell<*>.extCellAboveIndex
    get() = cellCellAboveIndex(this)
    set(value) = cellCellAboveIndex(this, value)

var Cell<*>.extColumn
    get() = cellColumn(this)
    set(value) = cellColumn(this, value)

var Cell<*>.extRow
    get() = cellRow(this)
    set(value) = cellRow(this, value)