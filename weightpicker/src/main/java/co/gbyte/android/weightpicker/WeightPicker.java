package co.gbyte.android.weightpicker;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    private static final String VALUE = "value";

    private final int MIN_MIN_VALUE = 0;
    private final int MAX_MAX_VALUE = 999949;

    private int mPrecision = 1;
    private int mBase = 1;
    private int mMinValue = MIN_MIN_VALUE;
    private int mMaxValue = MAX_MAX_VALUE;

    private NumberPicker mKilograms = null;
    private NumberPicker mHectograms = null;
    private boolean mInitialSetup = true;

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

        mHectograms = (NumberPicker) findViewById(R.id.hectograms);
        mKilograms  = (NumberPicker) findViewById(R.id.kilograms);
        mKilograms.setWrapSelectorWheel(false);

        if(attrs != null) {
            TypedArray a =
                    getContext().obtainStyledAttributes(attrs, R.styleable.WeightPicker, 0, 0);
            mPrecision = a.getInt(R.styleable.WeightPicker_precision, 1);
            mBase = a.getInt(R.styleable.WeightPicker_modDivisor, 1);

            setMinValue(a.getInt(R.styleable.WeightPicker_minValue, MIN_MIN_VALUE));
            setMaxValue(a.getInt(R.styleable.WeightPicker_maxValue, MAX_MAX_VALUE));
            mInitialSetup = false;

            setDisplayValues(mHectograms, mBase);
            EditText editText = findInput(mHectograms);
            if (editText != null) {
                editText.setFocusable(false);
                //editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }

            setValue(a.getInt(R.styleable.WeightPicker_initialValue, mMinValue));

            a.recycle();
        }

        mHectograms.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                mKilograms.setValue(getWholeKilograms(newValue));
                if (mListener != null) {
                    mListener.onValueChange(WeightPicker.this,
                                            oldValue * mPrecision,
                                            newValue * mPrecision);
                }
            }
        });

        mKilograms.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i0, int i1) {
                int oldValue = WeightPicker.this.getValue();
                mHectograms.setValue(mHectograms.getValue() + (i1 - i0) * mBase );
                if (mListener != null) {
                    mListener.onValueChange(WeightPicker.this,
                                            oldValue,
                                            WeightPicker.this.getValue());
                }
            }
        });
    }

    public void setMinValue(int value) {
        mMinValue = value <  MIN_MIN_VALUE ?  MIN_MIN_VALUE : value;
        mHectograms.setMinValue(toPickerValue(mMinValue, mPrecision));
        mKilograms.setMinValue(getWholeKilograms(mHectograms.getMinValue()));
        if(!mInitialSetup) {
            setDisplayValues(mHectograms, mBase);
        }
    }

    private int toPickerValue(int actualValue, int precision) {
        return (int) Math.round((double) actualValue / precision);
    }

    private int getWholeKilograms(int hectograms) {
        return (hectograms - hectograms % mBase) / mBase;
    }

    public void setMaxValue(int value) {
        mMaxValue = value > mMinValue ? value : mMinValue;
        mHectograms.setMaxValue(toPickerValue(mMaxValue, mPrecision));
        mKilograms.setMaxValue(getWholeKilograms(mHectograms.getMaxValue()));
        if(!mInitialSetup) {
            setDisplayValues(mHectograms, mBase);
        }
    }

    private void setDisplayValues(NumberPicker picker, int base) {
        int minVal = picker.getMinValue();
        int maxVal = picker.getMaxValue();

        int length = maxVal - minVal + 1;

        if (length <= 0) {
            return;
        }

        String displayValuesArray[] = new String[length];

        for (int i = 0; i < displayValuesArray.length; ++i) {
            displayValuesArray[i] = String.valueOf((picker.getMinValue() + i) % base);
        }
        picker.setDisplayedValues(displayValuesArray);
        picker.setWrapSelectorWheel(false);
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setValue (int value) {
        if (value < mMinValue) {
            value = mMinValue;
        } else if (value > mMaxValue) {
            value = mMaxValue;
        }
        mHectograms.setValue(toPickerValue(value, mPrecision));
        mKilograms.setValue(getWholeKilograms(mHectograms.getValue()));
    }

    public int getValue() {
        return mHectograms.getValue() * mPrecision;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        int value = getValue();
        Bundle state = new Bundle();

        state.putParcelable(SUPERSTATE, super.onSaveInstanceState());
        state.putInt(VALUE, value);

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
     * The callback interface used to indicate the value has been changed
     */
    public interface OnValueChangedListener {
        void onValueChange(WeightPicker picker, int oldValue, int newValue);
    }

    private EditText findInput (ViewGroup np) {
        int count = np.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = np.getChildAt(i);
            if (child instanceof ViewGroup) {
                findInput((ViewGroup) child);
            } else if (child instanceof EditText) {
                return (EditText) child;
            }
        }
        return null;
    }
}
