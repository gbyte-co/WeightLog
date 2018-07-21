package co.gbyte.weightlog.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException

import java.util.ArrayList
import java.util.UUID

import co.gbyte.weightlog.db.WeightBaseHelper
import co.gbyte.weightlog.db.WeightCursorWrapper
import co.gbyte.weightlog.db.WeightDbSchema.WeightTable

class WeightLab private constructor(context: Context) {

    private val mDb = WeightBaseHelper(context).writableDatabase

    companion object {

        @Volatile private var INSTANCE: WeightLab? = null

        fun getInstance(context: Context?): WeightLab =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: createWeightLab(context).also { INSTANCE = it }
                }

        private fun createWeightLab(context: Context?): WeightLab =
                WeightLab(context!!.applicationContext)
    }

    fun getWeights(): List<Weight>  {
        val weights: ArrayList<Weight> = ArrayList()
        val cursor = queryWeights(null, null,
                WeightTable.Cols.TIME + " DESC"
        )
        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                weights.add(cursor.weight)
                cursor.moveToNext()
            }
        } catch (e: SQLException) {
        } finally {
            cursor.close()
        }
        return weights
    }

    private var _lastWeight = Weight()
    val lastWeight: Int
        get() {
            val cursor = queryWeights(null, null, WeightTable.Cols.TIME + " DESC")
            try {
                if (cursor.count == 0) {
                    return 0
                }
                cursor.moveToFirst()
                _lastWeight = cursor.weight
            } finally {
                cursor.close()
            }
            return _lastWeight.weight
        }


    fun addWeight(weight: Weight) {
        val values = getContentValues(weight)
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
        var cursor: Cursor? = null
        try {
            cursor = mDb.query(
                    WeightTable.NAME,
                    null, // Columns = null selects all columns
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    orderClause  // orderBy
            )
        } catch (e: SQLException) {
        }
        // ToDo: test it:
        //cursor.close()
        return WeightCursorWrapper(cursor)
    }

    fun deleteWeight(id: UUID) {
        val uuidString = id.toString()
        mDb.delete(WeightTable.NAME, WeightTable.Cols.UUID + " = ?", arrayOf(uuidString))
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
