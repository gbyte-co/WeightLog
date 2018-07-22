package co.gbyte.weightlog.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException

import java.util.ArrayList
import java.util.UUID

import co.gbyte.weightlog.db.WeightBaseHelper
import co.gbyte.weightlog.db.WeightCursorWrapper
import co.gbyte.weightlog.db.WeightDbSchema.WeightTable.Cols.*
import co.gbyte.weightlog.db.WeightDbSchema.WeightTable.NAME

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
        val cursor = queryWeights(null, null, "$TIME DESC")
        cursor.use {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                weights.add(cursor.weight)
                cursor.moveToNext()
            }
        }
        return weights
    }

    fun getLastWeight(): Int {
        val cursor = queryWeights(null, null, "$TIME DESC")
        cursor.use {
            if (cursor.count == 0) {
                return 0
            }
            cursor.moveToFirst()
            return cursor.weight.weight
        }
    }

    fun addWeight(weight: Weight) {
        val values = getContentValues(weight)
        mDb.insert(NAME, null, values)
    }

    fun getWeight(id: UUID): Weight? {
        val cursor = queryWeights("$UUID = ?", arrayOf(id.toString()), null)
        cursor.use {
            if (cursor.count == 0) {
                return null
            }
            cursor.moveToFirst()
            return cursor.weight
        }
    }

    fun updateWeight(weight: Weight) {
        val uuidString = weight.id.toString()
        val values = getContentValues(weight)

        mDb.update(NAME, values, UUID, arrayOf(uuidString))
    }

    @SuppressLint("Recycle")
    private fun queryWeights(whereClause: String?,
                             whereArgs: Array<String>?,
                             orderClause: String?): WeightCursorWrapper {
        var cursor: Cursor? = null
        try {
            cursor = mDb.query(NAME,
                               null, // Columns = null selects all columns
                               whereClause,
                               whereArgs,
                               null,
                               null,
                               orderClause  // orderBy
                              )
        } catch (e: SQLException) {
        }
        return WeightCursorWrapper(cursor!!)
    }

    fun deleteWeight(id: UUID) {
        val uuidString = id.toString()
        mDb.delete(NAME, "$UUID  = ?", arrayOf(uuidString))
    }

    private fun getContentValues(weight: Weight): ContentValues {
        val values = ContentValues()
        values.put(UUID, weight.id.toString())
        values.put(TIME, weight.time.time)
        values.put(WEIGHT, weight.weight)
        values.put(NOTE, weight.note)

        return values
    }
}
