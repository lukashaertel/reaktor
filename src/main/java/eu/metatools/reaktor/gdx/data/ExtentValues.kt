package eu.metatools.reaktor.gdx.data

import com.badlogic.gdx.scenes.scene2d.ui.Value

data class ExtentValues(val top: Value, val left: Value, val bottom: Value, val right: Value) {
    constructor(top: Float, left: Float, bottom: Float, right: Float) :
            this(Value.Fixed(top), Value.Fixed(left), Value.Fixed(bottom), Value.Fixed(right))

    constructor(horizontal: Value, vertical: Value) : this(vertical, horizontal, vertical, horizontal)
    constructor(horizontal: Float, vertical: Float) : this(vertical, horizontal, vertical, horizontal)
    constructor(uniform: Value) : this(uniform, uniform, uniform, uniform)
    constructor(uniform: Float) : this(uniform, uniform, uniform, uniform)

    companion object {
        val zero = ExtentValues(Value.zero, Value.zero, Value.zero, Value.zero)
    }
}