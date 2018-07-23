package co.gbyte.weightlog.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import co.gbyte.weightlog.data.WeightSql.SQL_CREATE_ENTRIES
import co.gbyte.weightlog.data.WeightSql.SQL_DELETE_ENTRIES

class WeightBaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        private const val VERSION = 1
        private const val DB_NAME = "weightBase.db"
    }
}
