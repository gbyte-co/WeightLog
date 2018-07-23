package co.gbyte.weightlog.data

import android.database.Cursor
import android.database.CursorWrapper

import java.util.Date
import java.util.UUID

import co.gbyte.weightlog.data.WeightSql.WeightTable
import co.gbyte.weightlog.model.Weight

class WeightCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {

    val weight: Weight
        get() {
            val uuidString = getString(getColumnIndex(WeightTable.UUID))
            val time = getLong(getColumnIndex(WeightTable.CREATED_AT))
            val weightValue = getInt(getColumnIndex(WeightTable.WEIGHT))
            val note = getString(getColumnIndex(WeightTable.NOTE))

            val weight = Weight(UUID.fromString(uuidString))
            weight.time = Date(time)
            weight.weight = weightValue
            weight.note = note

            return weight
        }
}
