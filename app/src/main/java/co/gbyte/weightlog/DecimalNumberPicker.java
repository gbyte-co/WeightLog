package co.gbyte.weightlog;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.text.DecimalFormat;

/**
 * Created by walt on 7/12/14.
 *
 */

public class DecimalNumberPicker extends LinearLayout {

    /**
     * A no-op callback used in the constructor to avoid null checks
     * later in the code
     * (after: Google TimePicker)
     */

    private static final OnValueChangedListener NO_OP_CHANGE_LISTENER =
            new OnValueChangedListener() {
                public void onValueChanged(DecimalNumberPicker view, int integer,
                                           int decimal) {
                }
            };


    // state
    private int mCurrentInteger = 0; // 0-999
    private int mCurrentDecimal = 0; // 0-9

    // ui components
    private final NumberPicker mIntegerPicker;
    private final NumberPicker mDecimalPicker;

    // callbacks
    private OnValueChangedListener mOnValueChangedListener;

    /**
     * The callback interface used to indicate the value has been changed.
     */
    public interface OnValueChangedListener {
        /**
         * @param view The view associated with this listener.
         * @param integerPart The current integer part.
         * @param decimalPart The current decimalPart.
         */
        void onValueChanged(DecimalNumberPicker view,
                            int integerPart, int decimalPart);
    }

    /*
    public DecimalNumberPicker(Context context) {
        this(context, null);
    }

    public DecimalNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public DecimalNumberPicker(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }
    // this crashes the app. Did not investigate why
    */

    public DecimalNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.decimal_number_picker,
                this, // we are the parent
                true);

        // integer part
        mIntegerPicker = (NumberPicker) findViewById(R.id.integer_part);
        mIntegerPicker.setMinValue(0);
        mIntegerPicker.setMaxValue(999);
        mIntegerPicker.setOnValueChangedListener(
                new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker spinner, int oldVal, int newVal) {
                        mCurrentInteger = newVal;
                        onValueChanged();
                    }
                });

    // decimal_part
    mDecimalPicker = (NumberPicker) findViewById(R.id.decimal_part);
    // mDecimalPicker.setRange(0, 9);
    mDecimalPicker.setMinValue(0);
    mDecimalPicker.setMaxValue(9);

    // in widget/TimePicker.java:
    // mDecimalPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATER);
    mDecimalPicker.setOnValueChangedListener(
        new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker,
                                      int oldVal, int newVal) {
                if (newVal == (oldVal - getRange(mDecimalPicker))) {
                    mCurrentInteger ++;
                    //mIntegerPicker.setValue(mIntegerPicker.getValue() + 1);
                }
                if (newVal == (oldVal + getRange(mDecimalPicker))) {
                    mCurrentInteger --;
                    //mIntegerPicker.setValue(mIntegerPicker.getValue() - 1);
                }
                mIntegerPicker.setValue(mCurrentInteger);
                mCurrentDecimal = newVal;
                onValueChanged();
            }
        });
        setOnValueChangedListener(NO_OP_CHANGE_LISTENER);
    }

    private int getRange(NumberPicker picker) {
       return picker.getMaxValue() - picker.getMinValue();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mIntegerPicker.setEnabled(enabled);
        mDecimalPicker.setEnabled(enabled);
    }

    /**
     * Used to save / restore state of time picker
     */
    private static class SavedState extends BaseSavedState {

        private final int mInteger;
        private final int mDecimal;

        private SavedState(Parcelable superState, int integer, int decimal) {
            super(superState);
            mInteger = integer;
            mDecimal = decimal;
        }

        private SavedState(Parcel in) {
            super(in);
            mInteger = in.readInt();
            mDecimal = in.readInt();
        }

        public int getInteger() {
            return mInteger;
        }

        public int getDecimal() {
            return mDecimal;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mInteger);
            dest.writeInt(mDecimal);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mCurrentInteger, mCurrentDecimal);
    }

    @Override
    protected void  onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentInteger(ss.getInteger());
        setCurrentDecimal(ss.getDecimal());
    }

    /**
     * Set the callback that indicates the time has been adjusted by the user.
     */
    public void setOnValueChangedListener(OnValueChangedListener listener) {
        mOnValueChangedListener = listener;
    }

    public void init(double number) {
        mCurrentInteger = (int) number;
        number -= mCurrentInteger;
        mCurrentDecimal = (int) Math.round(number * 10);
        updateIntegerDisplay();
        updateDecimalDisplay();
    }

    /**
     * @return The current integer (0-999).
     */
    public Integer getCurrentInteger() {
        return mCurrentInteger;
    }

    /**
     * Set the current integer.
     */
    public void setCurrentInteger(Integer currentInteger) {
        this.mCurrentInteger = currentInteger;
        updateIntegerDisplay();
    }

    /**
     * @return The current decimal (0-9).
     */
    public Integer getCurrentDecimal() {
        return mCurrentDecimal;
    }

    /**
     * Set the current decimal.
     */
    public void setCurrentDecimal(Integer currentDecimal) {
        this.mCurrentDecimal = currentDecimal;
        updateDecimalDisplay();
    }

    @Override
    public int getBaseline() {
        return mIntegerPicker.getBaseline();
    }

    /**
     * Set the state of the spinners appropriate to the current values.
     */
    private void updateIntegerDisplay() {
        int currentInteger = mCurrentInteger;
        mIntegerPicker.setValue(currentInteger);
        onValueChanged();
    }

    private void updateDecimalDisplay() {
        mDecimalPicker.setValue(mCurrentDecimal);
        onValueChanged();
    }

    private void onValueChanged() {
        mOnValueChangedListener.onValueChanged(this, getCurrentInteger(),
                getCurrentDecimal());
    }
}
