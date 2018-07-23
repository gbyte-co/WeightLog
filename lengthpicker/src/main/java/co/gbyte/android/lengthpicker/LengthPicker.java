package co.gbyte.android.lengthpicker;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class LengthPicker extends LinearLayout {

    private static final int MIN_MIN_VALUE = 0;
    private static final int MAX_MAX_VALUE = Integer.MAX_VALUE;

    private static final String SUPERSTATE = "superState";
    private static final String LENGTH = "length";

    private int mMetricPrecision = 1;
    private int mMinValue = MIN_MIN_VALUE;
    private int mMaxValue = MAX_MAX_VALUE;
    private NumberPicker mMetric = null;
    private TextView mUnitsTv = null;

    public OnValueChangeListener mListener;

    public LengthPicker (Context context) {
        this(context, null);
    }

    public LengthPicker (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LengthPicker (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ((Activity) getContext())
                .getLayoutInflater()
                .inflate(R.layout.lengthpicker, this, true);

        mMetric = findViewById(R.id.metricLengthPicker);
        mUnitsTv = findViewById(R.id.metricLengthUnits);

        if(attrs != null) {
            TypedArray a = getContext()
                    .obtainStyledAttributes(attrs, R.styleable.LengthPicker, 0, 0);
            mMetricPrecision
                    = (a.getInt(R.styleable.LengthPicker_decimalPrecision, mMetricPrecision));

            setMinValue(a.getInt(R.styleable.LengthPicker_minValue, MIN_MIN_VALUE));
            setMaxValue(a.getInt(R.styleable.LengthPicker_maxValue, MAX_MAX_VALUE));
            setValue(a.getInt(R.styleable.LengthPicker_value, 0));
            mUnitsTv.setText(a.getString(R.styleable.LengthPicker_unitLabel));

            a.recycle();
        }

        mMetric.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i0, int i1) {
                if (mListener != null) {
                    mListener.onValueChange(LengthPicker.this,
                                            i0 * mMetricPrecision,
                                            i1 * mMetricPrecision);
                }
            }
        });
    }

    public void setMetricPrecision (int precision) {
        mMetricPrecision = precision;
    }

    public void setMinValue(int value) {
        mMinValue = value <= MIN_MIN_VALUE ? MAX_MAX_VALUE : value;
        mMetric.setMinValue(toPickerValue(mMinValue, mMetricPrecision));
    }

    private int toPickerValue (int value, int precision) {
        return (int) Math.round((double) value / precision);
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMaxValue(int value) {
        mMaxValue = value <= mMinValue ? mMinValue : value;
        mMetric.setMaxValue(toPickerValue(mMaxValue, mMetricPrecision));
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setValue(int value) {
        if (value <= mMinValue) {
            value  = mMinValue;
        } else if (value >= mMaxValue) {
            value = mMaxValue;
        }
        mMetric.setValue(toPickerValue(value, mMetricPrecision));
    }

    public int getValue() {
        return mMetric.getValue() * mMetricPrecision;
    }

    public void setUnitLabel(String text) {
        mUnitsTv.setText(text);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();
        state.putParcelable(SUPERSTATE, super.onSaveInstanceState());
        state.putInt(LENGTH, getValue());
        return(state);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable ss) {
        Bundle state = (Bundle) ss;
        super.onRestoreInstanceState(state.getParcelable(SUPERSTATE));
        setValue(state.getInt(LENGTH));
    }

    public interface OnValueChangeListener {
        void onValueChange(LengthPicker picker, int oldValue, int newValue);
    }
}
