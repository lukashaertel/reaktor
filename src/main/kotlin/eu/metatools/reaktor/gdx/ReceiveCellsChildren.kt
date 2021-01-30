package eu.metatools.reaktor.gdx

abstract class ReceiveCellsChildren {
    abstract fun cells(init: Receiver<VCell>)

    abstract fun children(init: Receiver<VActor<*>>)
}

typealias ReceiverCellsChildren = ReceiveCellsChildren.() -> Unit

fun ReceiverCellsChildren.toCells(): Receiver<VCell> = {
    val target = this

    this@toCells(object : ReceiveCellsChildren() {
        override fun cells(init: Receiver<VCell>) = init(target)
        override fun children(init: Receiver<VActor<*>>) = Unit
    })
}

fun ReceiverCellsChildren.toChildren(): Receiver<VActor<*>> = {
    val target = this

    this@toChildren(object : ReceiveCellsChildren() {
        override fun cells(init: Receiver<VCell>) = Unit
        override fun children(init: Receiver<VActor<*>>) = init(target)
    })
}
