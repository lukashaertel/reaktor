package eu.metatools.reaktor.gdx

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.utils.Align
import eu.metatools.reaktor.ex.consumeKey
import eu.metatools.reaktor.gdx.data.ExtentValues
import eu.metatools.reaktor.gdx.internals.extActor
import eu.metatools.reaktor.gdx.internals.extColumn
import eu.metatools.reaktor.gdx.internals.extRow
import eu.metatools.reaktor.gdx.utils.generateAtMostOne
import eu.metatools.reaktor.gdx.utils.tryReceive

open class VCell(
    val row: Int = defaultRow,
    val column: Int = defaultColumn,
    val minWidth: Value = defaultMinWidth,
    val minHeight: Value = defaultMinHeight,
    val prefWidth: Value = defaultPrefWidth,
    val prefHeight: Value = defaultPrefHeight,
    val maxWidth: Value = defaultMaxWidth,
    val maxHeight: Value = defaultMaxHeight,
    val space: ExtentValues = defaultSpace,
    val pad: ExtentValues = defaultPad,
    val fillX: Float = defaultFillX,
    val fillY: Float = defaultFillY,
    val align: Int = defaultAlign,
    val expandX: Int = defaultExpandX,
    val expandY: Int = defaultExpandY,
    val colSpan: Int = defaultColSpan,
    val uniformX: Boolean = defaultUniformX,
    val uniformY: Boolean = defaultUniformY,
    ref: (Cell<Actor>) -> Unit = defaultRef,
    key: Any? = consumeKey(),
    val actor: VActor<*>? = defaultActor,
) : VRef<Cell<Actor>>(ref, key) {
    companion object {
        const val defaultRow = 0
        const val defaultColumn = 0
        val defaultMinWidth: Value = Value.minWidth
        val defaultMinHeight: Value = Value.minHeight
        val defaultPrefWidth: Value = Value.prefWidth
        val defaultPrefHeight: Value = Value.prefHeight
        val defaultMaxWidth: Value = Value.maxWidth
        val defaultMaxHeight: Value = Value.maxHeight
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

        private const val ownProps = 19
    }


    override fun create() = Cell<Actor>()

    override fun assign(actual: Cell<Actor>) {
        actual.extColumn = column
        actual.extRow = row
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
        actual.extActor = actor?.make()
        super.assign(actual)
    }

    override val props = ownProps

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
        0 -> actual.extColumn
        1 -> actual.extRow
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
        18 -> actual.extActor
        else -> throw IndexOutOfBoundsException(prop)
    }

    override fun updateActual(prop: Int, actual: Cell<Actor>, value: Any?) {
        when (prop) {
            0 -> actual.extColumn = value as Int
            1 -> actual.extRow = value as Int
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
            18 -> actual.extActor = value as Actor?
            else -> throw IndexOutOfBoundsException(prop)
        }
    }

    override fun end(actual: Cell<Actor>) {
        // After update of cell, layout table again.
        actual.table?.layout()
        super.end(actual)
    }
}

inline fun cell(
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
    noinline ref: (Cell<Actor>) -> Unit = VRef.defaultRef,
    key: Any? = consumeKey(),
    generateActor: () -> Unit = {},
): VCell {
    val actor = generateAtMostOne<VActor<*>>(generateActor)
    return VCell(row,
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
        ref,
        key, actor).tryReceive()
}