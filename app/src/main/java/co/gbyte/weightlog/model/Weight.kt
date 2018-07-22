package co.gbyte.weightlog.model

import java.util.Date
import java.util.UUID

/**
 * Provides abstraction of a body weight reading
 */
data class Weight (
        val id: UUID = UUID.randomUUID(),
        //ToDo: rename to 'takenAt'
        var time: Date = Date(),
        private var _weight: Int = 0, // grams
        var note: String? = null
) {
    var weight: Int
        get() = _weight
        @Throws(IllegalArgumentException::class)
        set(weight) {
            if (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
                throw IllegalArgumentException(
                        "weight must be between $MAX_WEIGHT and $MAX_WEIGHT grams")
            }
            _weight = weight
        }

    // ToDo: find better solution: This shouldn't be in in this class(?)
    val weightStringKg: String
        get() {
            val weight = this.weight / 1000.0
            return weight.toString()
        }

    /**
     * Computes Body Mass Index if height and weight are set.
     * Otherwise returns 0.
     */
    fun bmi(): Double {
        return if (userHeight < MIN_HEIGHT || userHeight > MAX_HEIGHT || this.weight == 0) 0.0
            else weight.toDouble() * 1000 / (userHeight * userHeight)
    }

    companion object {
        // body height is stored in millimeters
        // ToDo: this should be a setting
        private var userHeight = 0

        private const val MIN_HEIGHT = 540
        private const val MAX_HEIGHT = 2750
        private const val AVERAGE_HEIGHT = 1690

        private const val MIN_WEIGHT = 2000
        private const val MAX_WEIGHT = 640000

        @Throws(IllegalArgumentException::class)
        fun setHeight(height: Int = AVERAGE_HEIGHT) {

            userHeight = if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
                AVERAGE_HEIGHT
                //throw IllegalArgumentException("height must be between " + MIN_HEIGHT
                //        + " and " + MAX_HEIGHT + " millimeters")
            } else {
                height
            }
        }
    }
}
