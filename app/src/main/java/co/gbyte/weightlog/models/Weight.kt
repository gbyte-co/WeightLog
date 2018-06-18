package co.gbyte.weightlog.models

import java.io.Serializable
import java.util.*

data class Weight(val uuid: UUID,
                  val time: Date,
                  val weight: Int,
                  val note: String) : Serializable {
}