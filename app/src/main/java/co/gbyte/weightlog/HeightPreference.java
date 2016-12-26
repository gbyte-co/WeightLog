package co.gbyte.weightlog;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

/**
 * Created by walt on 2/12/16.
 *
 */

public class HeightPreference extends DialogPreference {

    private int mLastHeight = 0;
    private Context mContext;
    private BodyLengthPicker mPicker = null;


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
        return (mPicker);
    }

    @Override
    protected  void onBindDialogView(View v) {
        super.onBindDialogView(v);

        // ToDo: reconsider using Weight class for storing min and max weight
        mPicker.setMinValue(mContext.getResources().getInteger(R.integer.min_human_height));
        mPicker.setMaxValue(mContext.getResources().getInteger(R.integer.max_human_height));

        /*
        if (mLastHeight == 0) {
            //mLastHeight = (mContext.getResources().getInteger(R.integer.average_human_height));
            mLastHeight = mPicker.getValue();
        }
        */
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
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
//        return (a.getInt(index,
//                mContext.getResources().getInteger(R.integer.average_human_height)));
        return 2750;
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        mLastHeight = 1800;
        //mLastHeight = restoreValue ? getPersistedInt(mLastHeight) : (Integer)defaultValue ;

    }
}
