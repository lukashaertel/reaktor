package eu.metatools.reaktor.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.Table
import eu.metatools.reaktor.gdx.utils.hidden


private val tableRound = hidden<Table, Boolean>("round")
private val tableColumns = hidden<Table, Int>("columns")
private val tableRows = hidden<Table, Int>("rows")

var Table.extRound
    get() = tableRound(this)
    set(value) = tableRound(this, value)

var Table.extColumns
    get() = tableColumns(this)
    set(value) = tableColumns(this, value)

var Table.extRows
    get() = tableRows(this)
    set(value) = tableRows(this, value)
