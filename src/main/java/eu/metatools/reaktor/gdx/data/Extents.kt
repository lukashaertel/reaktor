package eu.metatools.reaktor.gdx.data

data class Extents(val top: Float, val left: Float, val bottom: Float, val right: Float) {
    constructor(horizontal: Float, vertical: Float) : this(vertical, horizontal, vertical, horizontal)
    constructor(uniform: Float) : this(uniform, uniform, uniform, uniform)

    companion object {
        val zero = Extents(0f, 0f, 0f, 0f)
    }
}