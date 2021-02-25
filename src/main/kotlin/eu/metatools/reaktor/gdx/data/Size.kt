package eu.metatools.reaktor.gdx.data

data class Size(val width: Float, val height: Float) {
    constructor(uniform: Float) : this(uniform, uniform)

    companion object {
        val zero = Size(0f, 0f)
    }
}