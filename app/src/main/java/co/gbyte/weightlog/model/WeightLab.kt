package co.gbyte.weightlog.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import java.util.ArrayList
import java.util.UUID

import co.gbyte.weightlog.db.WeightBaseHelper
import co.gbyte.weightlog.db.WeightCursorWrapper
import co.gbyte.weightlog.db.WeightDbSchema.WeightTable

class WeightLab private constructor(context: Context) {

    private val mDb: SQLiteDatabase

    val weights: List<Weight>
        get() {
            val weights = ArrayList<Weight>()

            val cursor = queryWeights(// groupBy
                    null, null,
                    WeightTable.Cols.TIME + " DESC"
            )

            try {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    weights.add(cursor.weight)
                    cursor.moveToNext()
                }
            } finally {
                cursor.close()
            }
            return weights
        }

    val lastWeight: Int
        get() {
            var weight = Weight()

            val cursor = queryWeights(null, null, WeightTable.Cols.TIME + " DESC")
            try {
                if (cursor.count == 0) {
                    return 0
                }
                cursor.moveToFirst()
                weight = cursor.weight
            } finally {
                cursor.close()
            }
            return weight.weight
        }

    init {
        mDb = WeightBaseHelper(context).writableDatabase
    }

    fun addWeight(w: Weight) {
        val values = getContentValues(w)
        mDb.insert(WeightTable.NAME, null, values)
    }

    fun getWeight(id: UUID): Weight? {
        val cursor = queryWeights(WeightTable.Cols.UUID + " = ?",
                arrayOf(id.toString()), null)
        try {
            if (cursor.count == 0) {
                return null
            }

            cursor.moveToFirst()
            return cursor.weight
        } finally {
            cursor.close()
        }
    }

    fun updateWeight(weight: Weight) {
        val uuidString = weight.id.toString()
        val values = getContentValues(weight)

        mDb.update(WeightTable.NAME, values,
                WeightTable.Cols.UUID + " = ?",
                arrayOf(uuidString))
    }

    private fun queryWeights(whereClause: String?,
                             whereArgs: Array<String>?,
                             orderClause: String?): WeightCursorWrapper {
        val cursor = mDb.query(
                WeightTable.NAME, null, // Columns = null selects all columns
                whereClause,
                whereArgs, null, null, // having
                orderClause  // orderBy
        )
        return WeightCursorWrapper(cursor)
    }

    fun deleteWeight(id: UUID) {
        val uuidString = id.toString()
        mDb.delete(WeightTable.NAME, WeightTable.Cols.UUID + " = ?", arrayOf(uuidString))
    }

    companion object {
        private var sWeightLab: WeightLab? = null

        operator fun get(context: Context): WeightLab? {
            if (sWeightLab == null) {
                sWeightLab = WeightLab(context)
            }
            return sWeightLab
        }

        private fun getContentValues(weight: Weight): ContentValues {
            val values = ContentValues()
            values.put(WeightTable.Cols.UUID, weight.id.toString())
            values.put(WeightTable.Cols.TIME, weight.time!!.time)
            values.put(WeightTable.Cols.WEIGHT, weight.weight)
            values.put(WeightTable.Cols.NOTE, weight.note)

            return values
        }
    }
}
