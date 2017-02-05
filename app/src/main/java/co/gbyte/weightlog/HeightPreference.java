package co.gbyte.weightlog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import co.gbyte.android.lengthpicker.LengthPicker;
import co.gbyte.weightlog.model.WeightLab;

/**
 * Created by walt on 2/12/16.
 *
 */

public class HeightPreference extends DialogPreference {

    private int mLastHeight = 0;
    private Context mContext;
    //private BodyLengthPicker mPicker = null;
    private LengthPicker mPicker = null;
    private SharedPreferences mPrefs = null;


    public HeightPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(context.getText(R.string.height_pref_set));
        setNegativeButtonText(context.getText(R.string.height_pref_cancel));
    }

    @Override
    protected View onCreateDialogView() {
        mContext = getContext();
        mPicker = new LengthPicker(mContext);
        mPicker.setGravity(Gravity.CENTER);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return (mPicker);
    }

    @Override
    protected  void onBindDialogView(View v) {
        super.onBindDialogView(v);

        int minHeight = mContext.getResources().getInteger(R.integer.min_human_height);
        int maxHeight = mContext.getResources().getInteger(R.integer.max_human_height);
        mPicker.setMetricPrecision(10);
        mPicker.setMinValue(minHeight);
        mPicker.setMaxValue(maxHeight);
        mPicker.setUnitLabel(mContext.getString(R.string.unit_centimeters_short));

        String heightPrefKey = mContext.getString(R.string.height_pref_key);

        mLastHeight = mPrefs.getInt(heightPrefKey, 0);

        if (mLastHeight == 0) {
            int weight = WeightLab.get(mContext).getLastWeight();
            if (weight != 0) {
                // calculate height for the last weight and the ideal Bmi
                double bmi = mContext.getResources().getFraction(R.fraction.optimal_bmi, 1, 1);

                // ToDo: move to utils/Bmi class and make static
                mLastHeight = (int) ((Math.sqrt(weight * 1000 / bmi) + 5) / 10) * 10;

                if (mLastHeight > maxHeight) {
                    mLastHeight = maxHeight;
                }

                if (mLastHeight < minHeight) {
                    mLastHeight = minHeight;
                }

            } else {
                mLastHeight = mContext.getResources().getInteger(R.integer.average_human_height);
            }
        }
        mPicker.setValue(mLastHeight);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if(positiveResult) {
            if(callChangeListener(mPicker.getValue())) {

                // needed when user edits the text field and clicks OK
                mPicker.clearFocus();

                mLastHeight = mPicker.getValue();
                persistInt(mLastHeight);
            }
        } else if (mPrefs.getInt(mContext.getString(R.string.height_pref_key), 0) == 0) {
            // Bmi preference cannot be set if weight is not provided
            mPrefs.edit().putBoolean(mContext.getString(R.string.bmi_pref_key), false).apply();
        }
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        mLastHeight = restoreValue ? getPersistedInt(mLastHeight) : (Integer)defaultValue ;
    }
}
