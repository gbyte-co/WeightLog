package co.gbyte.android.weightpicker;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
    private int mOldValue = mMinValue;

    private NumberPicker mKilograms = null;
    private NumberPicker mHectograms = null;
    private EditText mPickerEdit = null;
    private boolean mInitialSetup = true;

    public OnValueChangedListener mListener;

    public WeightPicker(Context context) {
        this(context, null);
    }
    public WeightPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public WeightPicker(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final Activity activity = (Activity) getContext();
        activity.getLayoutInflater().inflate(R.layout.weightpicker, this, true);

        TextView separator = (TextView) findViewById(R.id.separator);
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        if (numberFormat instanceof DecimalFormat) {
            char sep = ((DecimalFormat) numberFormat).getDecimalFormatSymbols().getDecimalSeparator();
            separator.setText(String.format("%c", sep));
        }

        mHectograms = (NumberPicker) findViewById(R.id.hectograms);
        mKilograms  = (NumberPicker) findViewById(R.id.kilograms);
        mPickerEdit = (EditText) findViewById(R.id.weightPickerEdit);

        mPickerEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(i == EditorInfo.IME_ACTION_DONE) {
                    mPickerEdit.setVisibility(GONE);
                }
                mPickerEdit.clearFocus();
                return true;
            }
        });

        mPickerEdit.setSelectAllOnFocus(true);
        mPickerEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mOldValue = WeightPicker.this.getValue();
                    ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    mPickerEdit.selectAll();
                } else {
                    activity.getWindow().setSoftInputMode(WindowManager
                            .LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    double value = Double.parseDouble(mPickerEdit.getText().toString());
                    setValue((int) (value * mPrecision * mBase));

                    mListener.onValueChange(WeightPicker.this, mOldValue, getValue());
                    mPickerEdit.setVisibility(GONE);
                }
            }
        });

        OnLongClickListener longClickListener = new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mPickerEdit.setVisibility(VISIBLE);
                mPickerEdit.setText(String.format(Locale.getDefault(),
                                    "%.1f",
                                    (double) getValue() / (mPrecision * mBase)));
                mPickerEdit.requestFocus();

                return true;
            }
        };

        mHectograms.setOnLongClickListener(longClickListener);
        separator.setOnLongClickListener(longClickListener);
        mKilograms.setOnLongClickListener(longClickListener);

        EditText hectogramsEditText = findInput(mHectograms);
        if (hectogramsEditText != null) {
            hectogramsEditText.setFocusable(false);
        }

        EditText kilogramsEditText = findInput(mKilograms);
        if (kilogramsEditText != null) {
            kilogramsEditText.setFocusable(false);
        }

        // Find out the background colour of the host window and use it for the input EditText
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT
                && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            mPickerEdit.setBackgroundColor(typedValue.data);
        } else {
            // windowBackground is not a color, probably a drawable
            mPickerEdit.setBackground(ContextCompat.getDrawable(activity, typedValue.resourceId));
        }

        if(attrs != null) {
            TypedArray a =
                    getContext().obtainStyledAttributes(attrs, R.styleable.WeightPicker, 0, 0);
            mPrecision = a.getInt(R.styleable.WeightPicker_precision, 1);
            mBase = a.getInt(R.styleable.WeightPicker_modDivisor, 1);

            setMinValue(a.getInt(R.styleable.WeightPicker_minValue, MIN_MIN_VALUE));
            setMaxValue(a.getInt(R.styleable.WeightPicker_maxValue, MAX_MAX_VALUE));
            mInitialSetup = false;

            setDisplayValues(mHectograms, mBase);

            setValue(a.getInt(R.styleable.WeightPicker_initialValue, mMinValue));
            mKilograms.setWrapSelectorWheel(false);

            a.recycle();
        }

        mHectograms.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                mKilograms.setValue(getWholeKilograms(newValue));
                if (mListener != null) {
                    mListener.onValueChange(WeightPicker.this,
                                            oldValue * mPrecision * mBase,
                                            newValue * mPrecision * mBase);
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
