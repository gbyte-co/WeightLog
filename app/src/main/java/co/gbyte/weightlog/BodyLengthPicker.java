package co.gbyte.weightlog;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

/**
 * Created by walt on 18/12/16.
 *
 * A no-op callback used in the constructor to avoid null checks
 * later in the code
 * (after: Google TimePicker)
 */

public class BodyLengthPicker extends LinearLayout {

    private static final int MIN_VALUE = 500;
    private static final int MAX_VALUE = 2800;
    private static final int AVERAGE_VALUE = 1700;

    private static final String SUPERSTATE = "superState";
    private static final String HEIGHT = "height";

    private NumberPicker mNumberOfCmPicker = null;
    private OnValueChangedListener mListener = null;

    /* ToDo: implement an use
    private NumberPicker mNumberOfFeetPicker = null;
    private NumberPicker mNumberOfInchesPicker = null;
    private NumberPicker mNumberOfInchFourthsPicker = null;
    */

    // ToDo: remove final after 'imperial' implementation
    //private final boolean mImperial = false;

    public BodyLengthPicker (Context context) {
        this(context, null);
    }

    public BodyLengthPicker (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BodyLengthPicker (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

            ((Activity)getContext())
                    .getLayoutInflater()
                    .inflate(R.layout.body_length_picker, this, true);

        mNumberOfCmPicker = (NumberPicker) findViewById(R.id.body_length_picker_centimeters);
        // convert values to centimeters:
        mNumberOfCmPicker.setMinValue(MIN_VALUE  / 10);
        mNumberOfCmPicker.setMaxValue(MAX_VALUE  / 10);
        mNumberOfCmPicker.setValue(AVERAGE_VALUE / 10);
        mNumberOfCmPicker.setOnValueChangedListener(onChange);

        if(attrs != null) {
            TypedArray a = getContext()
                          .obtainStyledAttributes(attrs,R.styleable.BodyLengthPicker, 0, 0);
            setValue(a.getInt(R.styleable.BodyLengthPicker_initialValue, AVERAGE_VALUE / 10));
            setMinValue(a.getInt(R.styleable.BodyLengthPicker_minValue, MIN_VALUE / 10));
            setMaxValue(a.getInt(R.styleable.BodyLengthPicker_maxValue, MAX_VALUE / 10));
            a.recycle();
        }

        /*
        mNumberOfFeetPicker = (NumberPicker) findViewById(R.id.body_length_picker_feet);
        mNumberOfFeetPicker.setMinValue(0);
        mNumberOfFeetPicker.setMaxValue(8);
        mNumberOfInchesPicker = (NumberPicker) findViewById(R.id.body_length_picker_inches);
        mNumberOfInchesPicker.setMinValue(0);
        mNumberOfInchesPicker.setMaxValue(11);
        mNumberOfInchFourthsPicker =
                (NumberPicker) findViewById(R.id.body_length_picker_inch_fractions) ;
        mNumberOfInchFourthsPicker.setMinValue(0);
        mNumberOfInchFourthsPicker.setMaxValue(3);

        mImperial = true;
        setHeight(1730);
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();

        state.putParcelable(SUPERSTATE, super.onSaveInstanceState());
        state.putInt(HEIGHT, getValue());

        return(state);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable ss) {
        Bundle state = (Bundle) ss;

        super.onRestoreInstanceState(state.getParcelable(SUPERSTATE));

        setValue(state.getInt(HEIGHT));
    }

    /*
    public OnValueChangedListener getOnValueChangedListener() {
        return mListener;
    }

    public void setOnValueChangedListener(OnValueChangedListener mListener) {
        mListener = mListener;
    }
    */

    public void setValue(int value) throws IllegalArgumentException {
        throwIllegalHeightException(value);
        mNumberOfCmPicker.setValue(value / 10);
    }

    public void setMinValue(int value) {
        throwIllegalHeightException(value);
        mNumberOfCmPicker.setMinValue(value / 10);
    }

    public void setMaxValue(int value) {
        throwIllegalHeightException(value);
        mNumberOfCmPicker.setMaxValue(value / 10);
    }

    public int getValue() {
        return mNumberOfCmPicker.getValue() * 10;
    }

    private void throwIllegalHeightException(int value) throws IllegalArgumentException {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("value must be between " + MIN_VALUE
                    + " and " + MAX_VALUE);
        }
    }

    private NumberPicker.OnValueChangeListener onChange = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            int value = getValue() * 10;
            if (mListener != null) {
                mListener.onValueChange(value);
            }
        }
    };

    public interface OnValueChangedListener {
        void onValueChange(int argb);
    }
}



    /*
    private static final OnValueChangedListener NO_OP_CHANGE_LISTENER =
            new OnValueChangedListener() {
                public void onValueChanged(BodyLengthPicker view, int centimeters) {
                }
            };


    // state
    private int mCentimeters = 0; // 0-999

    private int mFeet        = 0; // 0-9
    private int mInches      = 0; // 0-11
    private int mInchQuoters = 0; // 0-3

    // ui components
    private final NumberPicker mNumbOfCentimetersPicker;


    // callbacks
    private OnValueChangedListener mOnValueChangedListener;

    /**
     * The callback interface used to indicate the value has been changed.
     */
    //public interface OnValueChangedListener {
        /**
         * @param view The view associated with this mListener.
         * @param integerPart The current integer part.
         * @param decimalPart The current decimalPart.
         */
    /*
        void onValueChanged(DecimalNumberPicker view,
                            int integerPart, int decimalPart);
    }
    */
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
    /*

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
    */

    /**
     * Used to save / restore state of time picker
     */
    /*
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
    */

    /**
     * Set the callback that indicates the time has been adjusted by the user.
     */
    /*
    public void setOnValueChangedListener(OnValueChangedListener mListener) {
        mOnValueChangedListener = mListener;
    }

    public void init(double number) {
        mCurrentInteger = (int) number;
        number -= mCurrentInteger;
        mCurrentDecimal = (int) Math.round(number * 10);
        updateIntegerDisplay();
        updateDecimalDisplay();
    }
    */

    /**
     * @return The current integer (0-999).
     */
    /*
    public Integer getCurrentInteger() {
        return mCurrentInteger;
    }
    */

    /**
     * Set the current integer.
     */
    /*
    public void setCurrentInteger(Integer currentInteger) {
        this.mCurrentInteger = currentInteger;
        updateIntegerDisplay();
    }
    */

    /**
     * @return The current decimal (0-9).
     */
    /*
    public Integer getCurrentDecimal() {
        return mCurrentDecimal;
    }
    */

    /**
     * Set the current decimal.
     */
    /*
    public void setCurrentDecimal(Integer currentDecimal) {
        this.mCurrentDecimal = currentDecimal;
        updateDecimalDisplay();
    }

    @Override
    public int getBaseline() {
        return mIntegerPicker.getBaseline();
    }
    */

    /**
     * Set the state of the spinners appropriate to the current values.
     */
    /*
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
*/
