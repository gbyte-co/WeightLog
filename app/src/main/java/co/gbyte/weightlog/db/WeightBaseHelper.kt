package co.gbyte.weightlog.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

import co.gbyte.weightlog.db.WeightDbSchema.WeightTable.Cols.*
import co.gbyte.weightlog.db.WeightDbSchema.WeightTable.NAME

class WeightBaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL( """CREATE TABLE $NAME (
                      |$_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                      |$UUID TEXT,
                      |$TIME TEXT,
                      |$WEIGHT INTEGER,
                      |$NOTE TEXT)"""
                .trimMargin()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $NAME")
        onCreate(db)
    }

    companion object {
        private const val VERSION = 1
        private const val DB_NAME = "weightBase.db"
    }
}
