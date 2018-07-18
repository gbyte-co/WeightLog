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

            if (weight < sWeightMin || weight > sWeightMax) {
                throw IllegalArgumentException("weight must be between " + sWeightMin
                        + " and " + sWeightMax + " grams")
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
        if (sHeight < sHeightMin || sHeight > sHeightMax) {
            return 0.0
        }

        return if (this.weight == 0) {
            0.0
        } else weight.toDouble() * 1000 / (sHeight * sHeight)

    }

    companion object {
        // body height is stored in millimeters
        private var sHeight = 0

        private val sHeightMin = 540
        private val sHeightMax = 2750

        private val sWeightMin = 2000
        private val sWeightMax = 640000

        @Throws(IllegalArgumentException::class)
        fun setHeight(height: Int) {

            if (height < sHeightMin || height > sHeightMax) {
                throw IllegalArgumentException("height must be between " + sHeightMin
                        + " and " + sHeightMax + " millimeters")
            }

            sHeight = height
        }

        fun resetHeight() {

            sHeight = 0
        }
    }
}
