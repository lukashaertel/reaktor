package eu.metatools.reaktor.ex

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * States by location.
 */
private val states = HashMap<Any, Any?>()

/**
 * Introduces a local state. When the state is changed, the implicit regeneration handle slot is invoked.
 */
fun <T> useState(initial: () -> T): ReadWriteProperty<Nothing?, T> {
    // Get invalidator and localize.
    val invalidator = invalidator()
    val location = localize() + peekKey()

    // Initialize empty locations.
    states.getOrPut(location, initial)

    @Suppress("unchecked_cast")
    return object : ReadWriteProperty<Nothing?, T> {
        override fun getValue(thisRef: Nothing?, property: KProperty<*>) =
            @Suppress("unchecked_cast")
            states.getValue(location) as T

        override fun setValue(thisRef: Nothing?, property: KProperty<*>, value: T) {
            // Get original value for comparison.
            val original = states[location] as T

            // Value has actually changed.
            if (original != value) {
                // Update register for the state.
                states[location] = value

                // Invalidate up.
                invalidator()
            }
        }
    }
}

fun <T> useState(initial: T) = useState { initial }