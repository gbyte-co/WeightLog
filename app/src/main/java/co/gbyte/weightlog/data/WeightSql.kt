package co.gbyte.weightlog.data

import co.gbyte.weightlog.data.WeightSql.WeightTable.CREATED_AT
import co.gbyte.weightlog.data.WeightSql.WeightTable.NOTE
import co.gbyte.weightlog.data.WeightSql.WeightTable.UUID
import co.gbyte.weightlog.data.WeightSql.WeightTable.WEIGHT
import co.gbyte.weightlog.data.WeightSql.WeightTable._ID
import co.gbyte.weightlog.data.WeightSql.WeightTable._TABLE_NAME

object WeightSql {

    object WeightTable {
        const val _TABLE_NAME = "weights"
        const val _ID = "id"
        const val UUID = "uuid"
        const val CREATED_AT = "time"
        const val WEIGHT = "weight"
        const val NOTE = "note"
    }

    val SQL_CREATE_ENTRIES = """CREATE TABLE $_TABLE_NAME
                      |(
                      |$_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                      |$UUID TEXT,
                      |$CREATED_AT TEXT,
                      |$WEIGHT INTEGER,
                      |$NOTE TEXT
                      |)""".trimMargin()

    val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $_TABLE_NAME"

    val SQL_SELECT_ALL = "SELECT * FROM $_TABLE_NAME ORDER BY $CREATED_AT DESC"
}