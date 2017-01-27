package co.gbyte.android.weightpicker;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
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

import static android.text.InputType.TYPE_CLASS_DATETIME;
import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_PHONE;
import static android.text.InputType.TYPE_DATETIME_VARIATION_TIME;
import static android.text.InputType.TYPE_NULL;

/**
 * Created by walt on 23/01/17.
 *
 */

public class WeightPicker extends RelativeLayout {
    private static final String SUPERSTATE = "superState";
    private static final String VALUE = "mValue";

    private final int MIN_MIN_VALUE = 0;
    private final int MAX_MAX_VALUE = 999949;
    private final int HECTO = 100;
    private final int DECA = 10;

    private NumberPicker mKilos = null;
    private NumberPicker mHectos = null;
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

        mKilos = (NumberPicker) findViewById(R.id.integer);
        mKilos.setWrapSelectorWheel(false);
        mKilos.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i0, int i1) {
                int startValue = mValue;
                mHectos.setValue(mHectos.getValue() + (i1 - i0) * DECA );
                updateValue();
                if (mListener != null) {
                    mListener.onValueChange(WeightPicker.this, startValue, mValue);
                }
            }
        });

        mHectos = (NumberPicker) findViewById(R.id.decimal);
        mHectos.setWrapSelectorWheel(false);
        EditText editText = findInput(mHectos);
        if(editText != null) {
            editText.setInputType(TYPE_CLASS_NUMBER);
        }
        mHectos.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i0, int i1) {
                mKilos.setValue(mHectos.getValue() / DECA);
                if (mListener != null) {
                    mListener.onValueChange(WeightPicker.this, i0 * HECTO, i1 *HECTO);
                }
            }
        });

        if(attrs != null) {
            TypedArray a =
                    getContext().obtainStyledAttributes(attrs, R.styleable.WeightPicker, 0, 0);

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

            mHectos.setMinValue(getHectograms(mMinValue));
            mHectos.setMaxValue(getHectograms(mMaxValue));
            mHectos.setValue(getHectograms(mValue));
            String hectoArray[] = new String[getHectograms(mMaxValue - mMinValue) + 1];
            for (int i = 0; i < hectoArray.length; ++i) {
                hectoArray[i] = String.valueOf((mHectos.getMinValue() + i) % 10);
            }
            mHectos.setDisplayedValues(hectoArray);

            mKilos.setMinValue(getWholeKilograms(mMinValue));
            mKilos.setMaxValue(getWholeKilograms(mMaxValue));

            updatePicker();
            a.recycle();
        }
    }

    private int getHectograms(int grams) {
        return (int) Math.round((double) grams / HECTO);
    }

    private int getWholeKilograms(int grams) {
        int hectograms = getHectograms(grams);
        return (hectograms - hectograms % DECA) / DECA;
    }

    private void updateValue() {
        mValue = mHectos.getValue() * HECTO;
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
        mHectos.setValue(getHectograms(mValue));
        mKilos.setValue(getWholeKilograms(getValue()));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        mValue = mHectos.getValue() * HECTO;
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

    // Credits: http://stackoverflow.com/questions/18944997/numberpicker-doesnt-work-with-keyboard?rq=1
    private EditText findInput(ViewGroup np) {
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
