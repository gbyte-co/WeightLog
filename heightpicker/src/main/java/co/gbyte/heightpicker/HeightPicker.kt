package co.gbyte.heightpicker

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import co.gbyte.android.heightpicker.R
import kotlinx.android.synthetic.main.heightpicker.view.*

class HeightPicker (context: Context, attrs : AttributeSet, defStyle : Int) :
        LinearLayout (context, attrs, defStyle) {

    companion object {
        const val MIN_MIN_VALUE = 0
        const val MAX_MAX_VALUE = Integer.MAX_VALUE
        const val SUPERSTATE = "superState"
        const val LENGTH = "length"
    }

    var value: Int
        get() =  metricNumberPicker!!.value * metricPrecision
        set(value) {
            when {
                value <= minValue -> minValue
                value >= maxValue -> maxValue
            }
            metricNumberPicker!!.value = toPickerValue(value, metricPrecision)
        }

    var metricPrecision = 1
    var minValue = MIN_MIN_VALUE
    set(value) {
            if (value <= MIN_MIN_VALUE) MAX_MAX_VALUE
            metricNumberPicker!!.minValue = toPickerValue(minValue, metricPrecision)
        }
    var maxValue = MAX_MAX_VALUE
        set(value) {
            if (value <= minValue)
            metricNumberPicker!!.maxValue = toPickerValue(maxValue, metricPrecision)
        }

    private var metricNumberPicker: NumberPicker? = null
    private var unitsTv: TextView? = null

    private var mListener: NumberPicker.OnValueChangeListener? = null

    init {
        (getContext() as Activity).layoutInflater.inflate(R.layout.heightpicker, this, true)

        metricNumberPicker = metricLengthPicker
        unitsTv = metricLengthUnits
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.HeightPicker, 0, 0)
        metricPrecision = a.getInt(R.styleable.HeightPicker_decimalPrecision, metricPrecision)
        minValue = a.getInt(R.styleable.HeightPicker_minValue, MIN_MIN_VALUE)
        maxValue = a.getInt(R.styleable.HeightPicker_maxValue, MAX_MAX_VALUE)
        value = a.getInt(R.styleable.HeightPicker_value, 0)
        unitsTv!!.text = a.getString(R.styleable.HeightPicker_unitLabel)

        metricNumberPicker!!.setOnValueChangedListener { numberPicker, i0, i1 ->
            if (mListener != null) {
                mListener!!.onValueChange(numberPicker,
                        i0 * metricPrecision,
                        i1 * metricPrecision)
            }
        }
        a.recycle()
    }

    constructor (context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    //constructor (context: Context) : this(context, null)

    private fun toPickerValue(value: Int, precision: Int): Int {
        return Math.round(value.toDouble() / precision).toInt()
    }

    fun setUnitLabel(text: String) {
        unitsTv!!.text = text
    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = Bundle()
        state.putParcelable(SUPERSTATE, super.onSaveInstanceState())
        state.putInt(LENGTH, value)
        return state
    }

    override fun onRestoreInstanceState(ss: Parcelable) {
        val state = ss as Bundle
        super.onRestoreInstanceState(state.getParcelable(SUPERSTATE))
        value = state.getInt(LENGTH)
    }

    interface OnValueChangeListener {
        fun onValueChange(picker: HeightPicker, oldValue: Int, newValue: Int)
    }
}