package co.gbyte.weightlog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.gbyte.weightlog.db.WeightDbSchema.WeightTable;

public class WeightBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DB_NAME = "weightBase.db";

    public WeightBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + WeightTable.NAME + "(" +
                "_id integer primary key autoincrement, "   +
                WeightTable.Cols.UUID   + " TEXT, " +
                WeightTable.Cols.TIME   + " TEXT, " +
                WeightTable.Cols.WEIGHT + " INT, "  +
                WeightTable.Cols.NOTE   + " TEXT"   +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
