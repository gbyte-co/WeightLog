package co.gbyte.weightlog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import co.gbyte.weightlog.model.WeightLab;

/**
 * Created by walt on 2/12/16.
 *
 */

public class HeightPreference extends DialogPreference {

    private int mLastHeight = 0;
    private Context mContext;
    private BodyLengthPicker mPicker = null;
    private SharedPreferences mPrefs = null;


    public HeightPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(context.getText(R.string.height_pref_set));
        setNegativeButtonText(context.getText(R.string.height_pref_cancel));
    }

    @Override
    protected View onCreateDialogView() {
        mContext = getContext();
        mPicker = new BodyLengthPicker(mContext);
        mPicker.setGravity(Gravity.CENTER);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return (mPicker);
    }

    @Override
    protected  void onBindDialogView(View v) {
        super.onBindDialogView(v);

        // ToDo: reconsider using Weight class for storing min and max weight
        int minHeight = mContext.getResources().getInteger(R.integer.min_human_height);
        int maxHeight = mContext.getResources().getInteger(R.integer.max_human_height);
        mPicker.setMinValue(minHeight);
        mPicker.setMaxValue(maxHeight);

        String heightPrefKey = mContext.getString(R.string.height_pref_key);

        mLastHeight = mPrefs.getInt(heightPrefKey, 0);

        if (mLastHeight == 0) {
            int weight = WeightLab.get(mContext).getLastWeight();
            if (weight != 0) {
                // calculate height for the last weight and the ideal BMI
                double bmi = mContext.getResources().getFraction(R.fraction.optimal_bmi, 1, 1);

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
            // BMI preference cannot be on if weight is not provided
            mPrefs.edit().putBoolean(mContext.getString(R.string.bmi_pref_key), false).apply();
        }
    }

    /*
    // ToDo: Don't I really need it?
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getInt(index,
                mContext.getResources().getInteger(R.integer.average_human_height)));
    }
    */

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        mLastHeight = restoreValue ? getPersistedInt(mLastHeight) : (Integer)defaultValue ;
    }
}
