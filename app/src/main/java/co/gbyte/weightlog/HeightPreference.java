package co.gbyte.weightlog;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by walt on 2/12/16.
 *
 */

public class HeightPreference extends DialogPreference {

    private static final int DEFAULT_HEIGHT = 169;
    private static final int MAX_HEIGHT = 275;
    private static final int MIN_HEIGHT = 40;

    private NumberPicker mPicker = null;
    private int mLastHeight;

    public HeightPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(context.getText(R.string.height_pref_set));
        setNegativeButtonText(context.getText(R.string.height_pref_cancel));
    }

    @Override
    protected View onCreateDialogView() {
        mPicker = new NumberPicker(getContext());
        return (mPicker);
    }

    @Override
    protected  void onBindDialogView(View v) {
        super.onBindDialogView(v);
        mPicker.setMinValue(MIN_HEIGHT);
        mPicker.setMaxValue(MAX_HEIGHT);
        mPicker.setValue(mLastHeight);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
         return (a.getInt(index, 0));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        mLastHeight = (restoreValue ? getPersistedInt(mLastHeight) : DEFAULT_HEIGHT);
    }


    /*
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // needed when user edits the text field and clicks OK
            mPicker.clearFocus();

            setValue(mPicker.getValue());
        }
    }

    private void setValue(int value) {
        if (shouldPersist()) {
            persistInt(value);
        }

        if (value != mLastHeight) {
            mLastHeight = value;
            notifyChanged();
        }
    }
    */

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if(positiveResult) {
            if(callChangeListener(mPicker.getValue())) {
                mLastHeight = mPicker.getValue();
                persistInt(mLastHeight);
            }
        }
    }
}

