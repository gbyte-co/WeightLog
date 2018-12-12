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

class HeightPicker (context: Context, attrs: AttributeSet?, defStyle : Int) :
        LinearLayout (context, attrs, defStyle) {

    companion object {
        private const val MIN_MIN_VALUE = 0
        private const val MAX_MAX_VALUE = Integer.MAX_VALUE
        private const val SUPERSTATE = "superState"
        private const val LENGTH = "length"
    }

    var value = 0
        get() =  metricNumberPicker.value * metricPrecision
        set(value) {
            field = when {
                value <= minValue -> minValue
                value >= maxValue -> maxValue
                else -> value
            }
            metricNumberPicker.value = toPickerValue(field, metricPrecision)
        }

    var metricPrecision = 1
    var minValue = MIN_MIN_VALUE
        set(value) {
            field = if (value >= maxValue) maxValue else value
            metricNumberPicker.minValue = toPickerValue(field, metricPrecision)
        }

    var maxValue = MAX_MAX_VALUE
        set(value) {
            field = if (value <= minValue) minValue else value
            metricNumberPicker.maxValue = toPickerValue(field, metricPrecision)
        }

    var unitsLabel = ""
        set(value) {
            unitsTv.text = value
        }

    private val metricNumberPicker: NumberPicker
    private val unitsTv: TextView

    init {
        (getContext() as Activity).layoutInflater.inflate(R.layout.heightpicker, this, true)

        metricNumberPicker = metricLengthPicker
        unitsTv = metricLengthUnits
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.HeightPicker, 0, 0)
        metricPrecision = a.getInt(R.styleable.HeightPicker_decimalPrecision, metricPrecision)
        minValue = a.getInt(R.styleable.HeightPicker_minValue, MIN_MIN_VALUE)
        maxValue = a.getInt(R.styleable.HeightPicker_maxValue, MAX_MAX_VALUE)
        value = a.getInt(R.styleable.HeightPicker_value, 0)
        unitsTv.text = a.getString(R.styleable.HeightPicker_unitLabel)

        // ToDo: fix the following. This can't be right
        val mListener: NumberPicker.OnValueChangeListener? = null

        metricNumberPicker.setOnValueChangedListener { numberPicker, i0, i1 ->
            mListener?.onValueChange(numberPicker, i0 * metricPrecision, i1 * metricPrecision)
        }
        a.recycle()
    }
    constructor (context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor (context: Context) : this(context, null)

    private fun toPickerValue(value: Int, precision: Int): Int {
        return Math.round(value.toDouble() / precision).toInt()
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

    /*
    ToDo: ???
    interface OnValueChangeListener {
        fun onValueChange(picker: HeightPicker, oldValue: Int, newValue: Int)
    }
    */
}