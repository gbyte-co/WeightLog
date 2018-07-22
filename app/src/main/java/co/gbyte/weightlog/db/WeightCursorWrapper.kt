package co.gbyte.weightlog.db

import android.database.Cursor
import android.database.CursorWrapper

import java.util.Date
import java.util.UUID

import co.gbyte.weightlog.db.WeightDbSchema.WeightTable
import co.gbyte.weightlog.model.Weight

/**
 * Created by walt on 20/10/16.
 *
 */

class WeightCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {

    val weight: Weight
        get() {
            val uuidString = getString(getColumnIndex(WeightTable.Cols.UUID))
            val time = getLong(getColumnIndex(WeightTable.Cols.TIME))
            val weightValue = getInt(getColumnIndex(WeightTable.Cols.WEIGHT))
            val note = getString(getColumnIndex(WeightTable.Cols.NOTE))

            val weight = Weight(UUID.fromString(uuidString))
            weight.time = Date(time)
            weight.weight = weightValue
            weight.note = note

            return weight
        }
}
