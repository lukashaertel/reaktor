package eu.metatools.reaktor.gdx

abstract class ReceiveActorChildren : Receive<VActor<*>>() {
    override fun receive(item: VActor<*>) =
        actor { this.receive(item) }

    abstract fun actor(init: Receiver<VActor<*>>)

    abstract fun children(init: Receiver<VActor<*>>)
}

typealias ReceiverActorChildren = ReceiveActorChildren.() -> Unit

fun ReceiverActorChildren.toActor(): Receiver<VActor<*>> = {
    val target = this

    this@toActor(object : ReceiveActorChildren() {
        override fun actor(init: Receiver<VActor<*>>) = init(target)
        override fun children(init: Receiver<VActor<*>>) = Unit
    })
}

fun ReceiverActorChildren.toChildren(): Receiver<VActor<*>> = {
    val target = this

    this@toChildren(object : ReceiveActorChildren() {
        override fun actor(init: Receiver<VActor<*>>) = Unit
        override fun children(init: Receiver<VActor<*>>) = init(target)
    })
}