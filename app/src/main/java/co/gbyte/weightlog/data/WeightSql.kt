package co.gbyte.weightlog.data

import co.gbyte.weightlog.data.WeightSql.WeightTable.CREATED_AT
import co.gbyte.weightlog.data.WeightSql.WeightTable.NOTE
import co.gbyte.weightlog.data.WeightSql.WeightTable.UUID
import co.gbyte.weightlog.data.WeightSql.WeightTable.WEIGHT
import co.gbyte.weightlog.data.WeightSql.WeightTable._ID
import co.gbyte.weightlog.data.WeightSql.WeightTable.TABLE_NAME

object WeightSql {

    object WeightTable {
        const val TABLE_NAME = "weights"
        const val _ID = "_id"
        const val UUID = "uuid"
        const val CREATED_AT = "time"
        const val WEIGHT = "weight"
        const val NOTE = "note"
    }

    val SQL_CREATE_ENTRIES = """CREATE TABLE $TABLE_NAME
                      |(
                      |$_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                      |$UUID TEXT,
                      |$CREATED_AT TEXT,
                      |$WEIGHT INTEGER,
                      |$NOTE TEXT
                      |)""".trimMargin()

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

    const val SQL_SELECT_ALL = "SELECT * FROM $TABLE_NAME ORDER BY $CREATED_AT DESC"
}