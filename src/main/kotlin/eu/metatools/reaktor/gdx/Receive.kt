package eu.metatools.reaktor.gdx

abstract class Receive<in T> {
    abstract fun receive(item: T)
    operator fun T.unaryPlus() = receive(this)
}

typealias Receiver<T> = Receive<T>.() -> Unit

class ReceiveOne<T>(private val target: (T) -> Unit) : Receive<T>() {
    private var set = false

    override fun receive(item: T) {
        require(!set) { "Value has already been assigned." }
        target(item)
        set = true
    }
}

class ReceiveMany<T>(private val target: (T) -> Unit) : Receive<T>() {
    override fun receive(item: T) {
        target(item)
    }
}

