package co.gbyte.weightlog.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import co.gbyte.weightlog.db.WeightDbSchema.WeightTable

class WeightBaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table " + WeightTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                WeightTable.Cols.UUID + " TEXT, " +
                WeightTable.Cols.TIME + " TEXT, " +
                WeightTable.Cols.WEIGHT + " INT, " +
                WeightTable.Cols.NOTE + " TEXT" +
                ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        private const val VERSION = 1
        private const val DB_NAME = "weightBase.db"
    }
}
