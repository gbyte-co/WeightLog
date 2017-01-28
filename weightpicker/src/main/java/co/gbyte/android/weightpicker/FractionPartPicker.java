package co.gbyte.android.weightpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * Created by walt on 27/01/17.
 *
 */

public class FractionPartPicker extends NumberPicker {

    private int mPrecision = 1;

    public FractionPartPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(attrs);
    }
    public FractionPartPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyAttributes(attrs);
    }

    private void applyAttributes(AttributeSet attrs) {
        if(attrs != null) {
            TypedArray a =
                    getContext().obtainStyledAttributes(attrs, R.styleable.WeightPicker, 0, 0);
            mPrecision = a.getInt(R.styleable.WeightPicker_precision, 1);

            int modDivisor = a.getInt(R.styleable.WeightPicker_modDivisor, 1);

            int value = a.getInt(R.styleable.WeightPicker_minValue, 0);
            int minValue = value > 0 ? value : 0;

            value = a.getInt(R.styleable.WeightPicker_maxValue, 0);
            int maxValue = value > minValue ? value : minValue;

            value = a.getInt(R.styleable.WeightPicker_initialValue, minValue);
            if (value < minValue) {
                value = minValue;
            } else if (value > maxValue) {
                value = maxValue;
            }

            setMinValue(toInternalValue(minValue));
            setMaxValue(toInternalValue(maxValue));
            setValue(toInternalValue(value));

            String displayValuesArray[] = new String[getMaxValue() - getMinValue() + 1];

            for (int i = 0; i < displayValuesArray.length; ++i) {
                displayValuesArray[i] = String.valueOf((getMinValue() + i) % modDivisor);
            }
            setDisplayedValues(displayValuesArray);
            setWrapSelectorWheel(false);

            a.recycle();
        }

        EditText input = findInput(this);

        if (input != null) {
            input.setFocusable(false);
            // irrelevant now because of the above:
            //input.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

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

    private int toInternalValue(int actualValue) {
        return (int) Math.round((double) actualValue / mPrecision);
    }

    public int getActualValue() {
        return getValue() * mPrecision;
    }
}
