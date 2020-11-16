package eu.metatools.kvr.gdx

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Align
import eu.metatools.kvr.gdx.data.ExtentValues
import eu.metatools.kvr.gdx.data.Ref
import eu.metatools.kvr.gdx.utils.hidden

data class VCell(
    val row: Int,
    val column: Int,
    val minWidth: Value,
    val minHeight: Value,
    val prefWidth: Value,
    val prefHeight: Value,
    val maxWidth: Value,
    val maxHeight: Value,
    val space: ExtentValues,
    val pad: ExtentValues,
    val fillX: Float,
    val fillY: Float,
    val align: Int,
    val expandX: Int,
    val expandY: Int,
    val colSpan: Int,
    val uniformX: Boolean,
    val uniformY: Boolean,
    val actor: VActor<*>?,
    override val ref: Ref<Cell<Actor>>?
) : VRef<Cell<Actor>>() {
    companion object {
        const val defaultRow = 0
        const val defaultColumn = 0
        val defaultMinWidth: Value = Value.minWidth;
        val defaultMinHeight: Value = Value.minHeight;
        val defaultPrefWidth: Value = Value.prefWidth;
        val defaultPrefHeight: Value = Value.prefHeight;
        val defaultMaxWidth: Value = Value.maxWidth;
        val defaultMaxHeight: Value = Value.maxHeight;
        val defaultSpace = ExtentValues.zero
        val defaultPad = ExtentValues.zero
        const val defaultFillX = 0f
        const val defaultFillY = 0f
        const val defaultAlign = Align.center
        const val defaultExpandX = 0
        const val defaultExpandY = 0
        const val defaultColSpan = 1
        const val defaultUniformX = false
        const val defaultUniformY = false
        val defaultActor: VActor<*>? = null

        val actor = hidden<Cell<*>, Actor?>("actor")
        val endRow = hidden<Cell<*>, Boolean>("endRow")
        val cellAboveIndex = hidden<Cell<*>, Int>("cellAboveIndex")
        val column = hidden<Cell<*>, Int>("column")
        val row = hidden<Cell<*>, Int>("row")
    }

    override fun create() = Cell<Actor>()

    override fun assign(actual: Cell<Actor>) {
        column(actual, column)
        row(actual, row)
        actual.minWidth(minWidth)
        actual.minHeight(minHeight)
        actual.prefWidth(prefWidth)
        actual.prefHeight(prefHeight)
        actual.maxWidth(maxWidth)
        actual.maxHeight(maxHeight)
        actual.space(space.top, space.left, space.bottom, space.right)
        actual.pad(pad.top, pad.left, pad.bottom, pad.right)
        actual.fill(fillX, fillY)
        actual.align(align)
        actual.expand(expandX, expandY)
        actual.colspan(colSpan)
        actual.uniform(uniformX)
        actual.uniform(uniformY)
        actor(actual, actor?.make())
        super.assign(actual)
    }

    override fun props() = 19

    override fun getOwn(prop: Int): Any? = when (prop) {
        0 -> column
        1 -> row
        2 -> minWidth
        3 -> minHeight
        4 -> prefWidth
        5 -> prefHeight
        6 -> maxWidth
        7 -> maxHeight
        8 -> space
        9 -> pad
        10 -> fillX
        11 -> fillY
        12 -> align
        13 -> expandX
        14 -> expandY
        15 -> colSpan
        16 -> uniformX
        17 -> uniformY
        18 -> actor
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun getActual(prop: Int, actual: Cell<Actor>): Any? = when (prop) {
        0 -> column(actual)
        1 -> row(actual)
        2 -> actual.minWidth
        3 -> actual.minHeight
        4 -> actual.prefWidth
        5 -> actual.prefHeight
        6 -> actual.maxWidth
        7 -> actual.maxHeight
        8 -> ExtentValues(actual.spaceTop, actual.spaceLeft, actual.spaceBottom, actual.spaceRight)
        9 -> ExtentValues(actual.padTop, actual.padLeft, actual.padBottom, actual.padRight)
        10 -> actual.fillX
        11 -> actual.fillY
        12 -> actual.align
        13 -> actual.expandX
        14 -> actual.expandY
        15 -> actual.colspan
        16 -> actual.uniformX
        17 -> actual.uniformY
        18 -> actor(actual)
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: Cell<Actor>, value: Any?) {
        when (prop) {
            0 -> column(actual, value as Int)
            1 -> row(actual, value as Int)
            2 -> actual.minWidth(value as Value)
            3 -> actual.minHeight(value as Value)
            4 -> actual.prefWidth(value as Value)
            5 -> actual.prefHeight(value as Value)
            6 -> actual.maxWidth(value as Value)
            7 -> actual.maxHeight(value as Value)
            8 -> actual.space((value as ExtentValues).top, value.left, value.bottom, value.right)
            9 -> actual.pad((value as ExtentValues).top, value.left, value.bottom, value.right)
            10 -> actual.fill(value as Float, actual.fillY)
            11 -> actual.fill(actual.fillX, value as Float)
            12 -> actual.align(value as Int)
            13 -> actual.expand(value as Int, actual.expandY)
            14 -> actual.expand(actual.expandX, value as Int)
            15 -> actual.colspan(value as Int)
            16 -> actual.uniform(value as Boolean, actual.uniformY)
            17 -> actual.uniform(actual.uniformX, value as Boolean)
            18 -> actor(actual, value as Actor?)
            else -> throw IndexOutOfBoundsException(prop)
        }
    }

    override fun end(actual: Cell<Actor>) {
        // After update of cell, layout table again.
        actual.table?.layout()
        super.end(actual)
    }
}

// TODO: Receiver parameter for type safe add?

inline fun cell(
    // VCell
    row: Int = VCell.defaultRow,
    column: Int = VCell.defaultColumn,
    minWidth: Value = VCell.defaultMinWidth,
    minHeight: Value = VCell.defaultMinHeight,
    prefWidth: Value = VCell.defaultPrefWidth,
    prefHeight: Value = VCell.defaultPrefHeight,
    maxWidth: Value = VCell.defaultMaxWidth,
    maxHeight: Value = VCell.defaultMaxHeight,
    space: ExtentValues = VCell.defaultSpace,
    pad: ExtentValues = VCell.defaultPad,
    fillX: Float = VCell.defaultFillX,
    fillY: Float = VCell.defaultFillY,
    align: Int = VCell.defaultAlign,
    expandX: Int = VCell.defaultExpandX,
    expandY: Int = VCell.defaultExpandY,
    colSpan: Int = VCell.defaultColSpan,
    uniformX: Boolean = VCell.defaultUniformX,
    uniformY: Boolean = VCell.defaultUniformY,

    // VRef
    @Suppress("unchecked_cast")
    ref: Ref<Cell<Actor>>? = VRef.defaultRef as Ref<Cell<Actor>>?,

    // VCell
    actor: () -> Unit
) = constructParent<VActor<*>, VCell>(actor) {
    VCell(
        row,
        column,
        minWidth,
        minHeight,
        prefWidth,
        prefHeight,
        maxWidth,
        maxHeight,
        space,
        pad,
        fillX,
        fillY,
        align,
        expandX,
        expandY,
        colSpan,
        uniformX,
        uniformY,
        it.singleOrNull(),
        ref
    )
}