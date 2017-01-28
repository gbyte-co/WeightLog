package co.gbyte.android.weightpicker;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by walt on 23/01/17.
 *
 */

public class WeightPicker extends RelativeLayout {
    private static final String SUPERSTATE = "superState";
    private static final String VALUE = "mValue";

    private final int MIN_MIN_VALUE = 0;
    private final int MAX_MAX_VALUE = 999949;
    private final int PRECISION = 100;
    private final int DECA = 10;

    private NumberPicker mKilograms = null;
    private NumberPicker mHectograms = null;
    private int mMinValue = MIN_MIN_VALUE;
    private int mMaxValue = MAX_MAX_VALUE;

    // state
    private int mValue = MIN_MIN_VALUE;

    public OnValueChangedListener mListener;

    public WeightPicker(Context context) {
        this(context, null);
    }
    public WeightPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public WeightPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        ((Activity)getContext()).getLayoutInflater().inflate(R.layout.weightpicker, this, true);

        TextView separator = (TextView) findViewById(R.id.separator);
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        if (numberFormat instanceof DecimalFormat) {
            char s = ((DecimalFormat) numberFormat).getDecimalFormatSymbols().getDecimalSeparator();
            separator.setText(String.format("%c", s));
        }

        mHectograms = (FractionPartPicker) findViewById(R.id.decimal);

        /*
        EditText editText = findInput(mHectograms);
        if(editText != null) {
            editText.setInputType(TYPE_CLASS_NUMBER);
        }
        mHectograms.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i0, int i1) {
                mKilograms.setValue(mHectograms.getValue() / DECA);
                if (mListener != null) {
                    mListener.onValueChange(WeightPicker.this, i0 * PRECISION, i1 * PRECISION);
                }
            }
        });
        */

        if(attrs != null) {
            TypedArray a =
                    getContext().obtainStyledAttributes(attrs, R.styleable.WeightPicker, 0, 0);

            /*
            int val = a.getInt(R.styleable.WeightPicker_minValue, MIN_MIN_VALUE);
            if (val < MIN_MIN_VALUE) {
                mMinValue = MIN_MIN_VALUE;
            } else if (val > MAX_MAX_VALUE) {
                mMinValue = MAX_MAX_VALUE;
            } else {
                mMinValue = val;
            }

            val = a.getInt(R.styleable.WeightPicker_maxValue, MAX_MAX_VALUE);
            if (val < mMinValue) {
                mMaxValue = mMinValue;
            } else if (val > MAX_MAX_VALUE) {
                mMaxValue = MAX_MAX_VALUE;
            } else {
                mMaxValue = val;
            }

            val = a.getInt(R.styleable.WeightPicker_initialValue, mMinValue);
            setValue(val);

            mHectograms.setMinValue(getHectograms(mMinValue));
            mHectograms.setMaxValue(getHectograms(mMaxValue));
            mHectograms.setValue(getHectograms(mValue));
            String hectoArray[] = new String[getHectograms(mMaxValue - mMinValue) + 1];
            for (int i = 0; i < hectoArray.length; ++i) {
                hectoArray[i] = String.valueOf((mHectograms.getMinValue() + i) % 10);
            }
            mHectograms.setDisplayedValues(hectoArray);

            mKilograms.setMinValue(getWholeKilograms(mMinValue));
            mKilograms.setMaxValue(getWholeKilograms(mMaxValue));

            updatePicker();
            a.recycle();
            */
        }

        mKilograms = (NumberPicker) findViewById(R.id.integer);
        mKilograms.setWrapSelectorWheel(false);
        mKilograms.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i0, int i1) {
                int startValue = mValue;
                mHectograms.setValue(mHectograms.getValue() + (i1 - i0) * DECA );
                updateValue();
                if (mListener != null) {
                    mListener.onValueChange(WeightPicker.this, startValue, mValue);
                }
            }
        });
    }

    private int getHectograms(int grams) {
        return (int) Math.round((double) grams / PRECISION);
    }

    private int getWholeKilograms(int grams) {
        int hectograms = getHectograms(grams);
        return (hectograms - hectograms % DECA) / DECA;
    }

    private void updateValue() {
        mValue = mHectograms.getValue() * PRECISION;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        if (value < mMinValue) {
            mValue = mMinValue;
        } else if (value > mMaxValue) {
            mValue = mMaxValue;
        } else {
            mValue = value;
        }
        updatePicker();
    }

    private void updatePicker() {
        mHectograms.setValue(getHectograms(mValue));
        mKilograms.setValue(getWholeKilograms(getValue()));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        mValue = mHectograms.getValue() * PRECISION;
        Bundle state = new Bundle();

        state.putParcelable(SUPERSTATE, super.onSaveInstanceState());
        state.putInt(VALUE, mValue);

        return (state);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable st) {
        Bundle state = (Bundle) st;
        super.onRestoreInstanceState(state.getParcelable(SUPERSTATE));
        setValue(state.getInt(VALUE));
    }

    public OnValueChangedListener getOnValueChangedListener() {
        return mListener;
    }

    public void setOnValueChangedListener (OnValueChangedListener listener) {
        mListener = listener;
    }

    /**
     * The callback interface used to indicate the mValue has been changed
     */
    public interface OnValueChangedListener {
        void onValueChange(WeightPicker picker, int i0, int i1);
    }
}
