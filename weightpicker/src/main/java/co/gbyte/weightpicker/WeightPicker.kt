package co.gbyte.weightpicker

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.RelativeLayout
import android.widget.TextView
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import android.widget.EditText
import android.view.inputmethod.EditorInfo
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

class WeightPicker constructor(context: Context, attrs: AttributeSet?, defStyle: Int)
        : RelativeLayout(context, attrs, defStyle) {

    companion object {
        private const val SUPERSTATE = "superState"
        private const val VALUE = "value"
        private const val MIN_MIN_VALUE = 0
        private const val MAX_MAX_VALUE = 999949
    }

    var value: Int = 0
        set(value) {
            field = when {
                value < _minValue -> _minValue
                value > _maxValue -> _maxValue
                else -> value
            }
            _hectograms.value = toPickerValue(field)
            _kilograms.value = getWholeKilograms(value)
        }

    private fun toPickerValue(actualValue: Int): Int {
        return Math.round(actualValue.toDouble() / _precision ).toInt()
    }

    private fun getWholeKilograms (grams: Int): Int{
        return (grams - grams % (_base * _precision)) / (_base * _precision)
    }

    private var _minValue = MIN_MIN_VALUE
        set(value) {
            field = when {
                value < MIN_MIN_VALUE -> MIN_MIN_VALUE
                value > _maxValue -> _maxValue
                value > MAX_MAX_VALUE -> MAX_MAX_VALUE
                else -> value
            }
            _hectograms.minValue = toPickerValue(field)
            _kilograms.minValue = getWholeKilograms(field)
        }

    private var _maxValue = MAX_MAX_VALUE
        set(value) {
            field = when {
                value > MAX_MAX_VALUE -> MAX_MAX_VALUE
                value < _minValue -> _minValue
                value < MIN_MIN_VALUE -> MIN_MIN_VALUE
                else -> value
            }
            _hectograms.maxValue = toPickerValue(field)
            _kilograms.maxValue = getWholeKilograms(field)
            if(!_initialSetup) setDisplayValues(_hectograms, _base)
        }

    lateinit var onValueChangedListener: OnValueChangedListener

    private var _precision = 1
    private var _base = 1
    private var _oldValue = _minValue
    private val _hectograms: NumberPicker
    private val _kilograms: NumberPicker
    private val _pickerEdit: EditText
    private var _initialSetup = true

    constructor (context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor (context: Context) : this(context, null)
    init {
        val activity = context as Activity
        activity.layoutInflater.inflate(R.layout.weightpicker, this, true)

        val separator: TextView = findViewById(R.id.separator)
        val numberFormat = NumberFormat.getInstance(Locale.getDefault())
        if (numberFormat is DecimalFormat) {
            val sep = numberFormat.decimalFormatSymbols.decimalSeparator
            separator.text = String.format("%c", sep)
        }

        _kilograms = findViewById(R.id.kilograms)
        _hectograms = findViewById(R.id.hectograms)
        _pickerEdit = findViewById(R.id.weightPickerEdit)

        _pickerEdit.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                _pickerEdit.visibility = View.GONE
            }
            _pickerEdit.clearFocus()
            true
        }

        _pickerEdit.setSelectAllOnFocus(true)

        _pickerEdit.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                _oldValue = this@WeightPicker.value
                (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                _pickerEdit.selectAll()
            } else {
                activity.window.setSoftInputMode(WindowManager
                        .LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                val value = java.lang.Double.parseDouble(_pickerEdit.text.toString())
                this@WeightPicker.value = (value * _precision * _base).toInt()

                onValueChangedListener
                        .onValueChange(this@WeightPicker, _oldValue, this@WeightPicker.value)

                _pickerEdit.visibility = View.GONE
            }
        }

        _pickerEdit.setSelectAllOnFocus(true)

        val longClickListener = OnLongClickListener {
            _pickerEdit.visibility = View.VISIBLE
            _pickerEdit.setText(
                    String.format(Locale.getDefault(),
                    "%.1f",
                    value.toDouble() / (_precision * _base)))
            _pickerEdit.requestFocus()
            true
        }

        _hectograms.setOnLongClickListener(longClickListener)
        separator.setOnLongClickListener(longClickListener)
        _kilograms.setOnLongClickListener(longClickListener)

        val hectogramsEditText = findInput(_hectograms)

        if (hectogramsEditText != null) {
            hectogramsEditText.isFocusable = false
        }

        val kilogramsEditText = findInput(_kilograms)
        if (kilogramsEditText != null) {
            kilogramsEditText.isFocusable = false
        }

        // Find out the background colour of the host window and use it for the input EditText
        val typedValue = TypedValue()
        activity.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT
                && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            _pickerEdit.setBackgroundColor(typedValue.data)
        } else {
            // windowBackground is not a color, probably a drawable
            _pickerEdit.background = ContextCompat.getDrawable(activity, typedValue.resourceId)
        }

        if (attrs != null) {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.WeightPicker, 0, 0)
            _precision = a.getInt(R.styleable.WeightPicker_precision, 1)
            _base = a.getInt(R.styleable.WeightPicker_modDivisor, 1)

            _minValue = a.getInt(R.styleable.WeightPicker_minValue, MIN_MIN_VALUE)
            _maxValue = a.getInt(R.styleable.WeightPicker_maxValue, MAX_MAX_VALUE)
            _initialSetup = false

            setDisplayValues(_hectograms, _base)

            value = a.getInt(R.styleable.WeightPicker_initialValue, _minValue)
            _kilograms.wrapSelectorWheel = false

            a.recycle()
        }

        _hectograms.setOnValueChangedListener { _, _, i1 ->
            _kilograms.value = getWholeKilograms(i1 * _precision)
            val oldValue = value
            value = i1 * _precision
            onValueChangedListener.onValueChange(this@WeightPicker, oldValue, this@WeightPicker.value)
        }

        _kilograms.setOnValueChangedListener { _, i0, i1 ->
            val oldValue = value
            value += (i1 - i0) * 1000
            _hectograms.value = toPickerValue(value)
            onValueChangedListener.onValueChange(this@WeightPicker, oldValue, this@WeightPicker.value)
        }
    }

    private fun findInput(np: ViewGroup): EditText? {
        val count = np.childCount
        for (i in 0 until count) {
            val child = np.getChildAt(i)
            if (child is ViewGroup) {
                findInput(child)
            } else if (child is EditText) {
                return child
            }
        }
        return null
    }

    private fun setDisplayValues(picker: NumberPicker, base: Int) {
        val minVal = picker.minValue
        val maxVal = picker.maxValue
        val length = maxVal - minVal + 1

        if (length <= 0) {
            return
        }

        val displayValuesArray = arrayOfNulls<String>(length)

        for (i in displayValuesArray.indices) {
            displayValuesArray[i] = ((picker.minValue + i) % base).toString()
        }
        picker.displayedValues = displayValuesArray
        picker.wrapSelectorWheel = false
    }

    override fun onSaveInstanceState(): Parcelable? {
        val value = value
        val state = Bundle()

        state.putParcelable(SUPERSTATE, super.onSaveInstanceState())
        state.putInt(VALUE, value)

        return state
    }

    override fun onRestoreInstanceState(st: Parcelable) {
        val state = st as Bundle
        super.onRestoreInstanceState(state.getParcelable(SUPERSTATE))
        value = state.getInt(VALUE)
    }

    /**
     * The callback interface used to indicate the value has been changed
     */
    interface OnValueChangedListener {
        fun onValueChange(picker: WeightPicker, oldValue: Int, newValue: Int)
    }
}
