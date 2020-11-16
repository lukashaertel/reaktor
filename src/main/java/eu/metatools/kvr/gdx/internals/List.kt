package eu.metatools.kvr.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.List
import eu.metatools.kvr.gdx.utils.hidden


private val listAlign = hidden<List<*>, Int>("alignment")
private val listTypeToSelect = hidden<List<*>, Boolean>("typeToSelect")

var List<*>.extAlign
    get() = listAlign(this)
    set(value) = listAlign(this, value)

var List<*>.extTypeToSelect
    get() = listTypeToSelect(this)
    set(value) = listTypeToSelect(this, value)