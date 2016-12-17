package co.gbyte.weightlog.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.gbyte.weightlog.db.WeightBaseHelper;
import co.gbyte.weightlog.db.WeightCursorWrapper;
import co.gbyte.weightlog.db.WeightDbSchema.WeightTable;

/**
 * Created by walt on 19/10/16.
 *
 */

public class WeightLab {
    private static WeightLab sWeightLab;

    private Context mContext;
    private SQLiteDatabase mDb;

    public static WeightLab get(Context context) {
        if (sWeightLab == null) {
            sWeightLab = new WeightLab(context);
        }
        return sWeightLab;
    }

    private WeightLab(Context context) {
        mContext = context.getApplicationContext();
        mDb = new WeightBaseHelper(mContext).getWritableDatabase();
    }

    public void addWeight(Weight w) {
        ContentValues values = getContentValues(w);
        mDb.insert(WeightTable.NAME, null, values);
    }

    public List<Weight> getWeights() {
        List<Weight> weights = new ArrayList<>();

        WeightCursorWrapper cursor = queryWeights(null, null,
                WeightTable.Cols.TIME + " DESC"
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                weights.add(cursor.getWeight());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return weights;
    }

    public Weight getWeight(UUID id) {
        WeightCursorWrapper cursor = queryWeights(
                WeightTable.Cols.UUID + " = ?",
                new String[] { id.toString() },
                null
        );

        try {
            if(cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getWeight();
        } finally {
            cursor.close();
        }
    }

    public int getLastWeight() {
        Weight weight = new Weight();

        WeightCursorWrapper cursor = queryWeights(null, null,
                WeightTable.Cols.TIME + " DESC"
                );

        try {
            if (cursor.getCount() == 0) {
                return 0;
            }
            cursor.moveToFirst();
            weight = cursor.getWeight();
        } finally {
            cursor.close();
        }
        return weight.getWeight();
    }

    public void updateWeight(Weight weight) {
        String uuidString = weight.getId().toString();
        ContentValues values = getContentValues(weight);

        mDb.update(WeightTable.NAME, values,
                   WeightTable.Cols.UUID + " = ?",
                   new String[] { uuidString });
    }

    private static ContentValues getContentValues(Weight weight) {
        ContentValues values = new ContentValues();
        values.put(WeightTable.Cols.UUID, weight.getId().toString());
        values.put(WeightTable.Cols.TIME, weight.getTime().getTime());
        values.put(WeightTable.Cols.WEIGHT, weight.getWeight());
        values.put(WeightTable.Cols.NOTE, weight.getNote());

        return values;
    }

    private WeightCursorWrapper queryWeights(String whereClause,
                                             String[] whereArgs,
                                             String orderClause) {
        Cursor cursor = mDb.query (
                WeightTable.NAME,
                null, // Columns = null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                orderClause  // orderBy
        );
        return new WeightCursorWrapper(cursor);
    }

    public void deleteWeight(UUID id) {
        String uuidString = id.toString();
        mDb.delete(WeightTable.NAME, WeightTable.Cols.UUID + " = ?", new String[] { uuidString });
    }
}
