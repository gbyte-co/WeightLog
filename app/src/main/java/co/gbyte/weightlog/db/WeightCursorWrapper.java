package co.gbyte.weightlog.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import co.gbyte.weightlog.db.WeightDbSchema.WeightTable;
import co.gbyte.weightlog.model.Weight;

/**
 * Created by walt on 20/10/16.
 *
 */

public class WeightCursorWrapper extends CursorWrapper {
    public WeightCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Weight getWeight() {
        String uuidString = getString(getColumnIndex(WeightTable.Cols.UUID));
        long time = getLong(getColumnIndex(WeightTable.Cols.TIME));
        int weightValue = getInt(getColumnIndex(WeightTable.Cols.WEIGHT));
        String note = getString(getColumnIndex(WeightTable.Cols.NOTE));

        Weight weight = new Weight(UUID.fromString(uuidString));
        weight.setTime(new Date(time));
        weight.setWeight(weightValue);
        weight.setNote(note);

        return weight;
    }
}
