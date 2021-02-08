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
    val location = localize()

    // Initialize empty locations.
    @Suppress("unchecked_cast")
    val atAccess = states.getOrPut(location, initial) as T

    @Suppress("unchecked_cast")
    return object : ReadWriteProperty<Nothing?, T> {
        override fun getValue(thisRef: Nothing?, property: KProperty<*>) =
            atAccess

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