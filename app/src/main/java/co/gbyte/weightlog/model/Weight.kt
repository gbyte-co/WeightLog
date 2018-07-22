package co.gbyte.weightlog.model

import java.util.Date
import java.util.UUID

/**
 * Provides abstraction of body weight and calculates Body Mass Index.
 */
class Weight @JvmOverloads constructor(val id: UUID = UUID.randomUUID()) {
    var time: Date? = null
    // body weight is stored in grams
    var weight: Int = 0
        @Throws(IllegalArgumentException::class)
        set(weight) {
            if (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
                throw IllegalArgumentException("weight must be between " + MIN_WEIGHT
                        + " and " + MAX_WEIGHT + " grams")
            }

            field = weight
        }
    var note: String? = null

    // ToDo: find better solution: This shouldn't be in in this class(?)
    val weightStringKg: String
        get() {
            val weight = this.weight / 1000.0
            return weight.toString()
        }

    init {
        time = Date()
    }

    /**
     * Computes Body Mass Index if height and weight are set.
     * Otherwise returns 0.
     */
    fun bmi(): Double {
        if (userHeight < MIN_HEIGHT || userHeight > MAX_HEIGHT) {
            return 0.0
        }
        return if (this.weight == 0)  0.0 else weight.toDouble() * 1000 / (userHeight * userHeight)
    }

    companion object {
        // body height is stored in millimeters
        // ToDo: this should be a setting
        private var userHeight = 0

        private const val MIN_HEIGHT = 540
        private const val MAX_HEIGHT = 2750

        private const val MIN_WEIGHT = 2000
        private const val MAX_WEIGHT = 640000

        @Throws(IllegalArgumentException::class)
        fun setHeight(height: Int) {

            if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
                throw IllegalArgumentException("height must be between " + MIN_HEIGHT
                        + " and " + MAX_HEIGHT + " millimeters")
            }
            userHeight = height
        }
    }
}
