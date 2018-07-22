package co.gbyte.weightlog

import android.content.Context
import android.content.SharedPreferences
import android.preference.DialogPreference
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View

import co.gbyte.android.lengthpicker.LengthPicker
import co.gbyte.weightlog.model.WeightLab

class HeightPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {

    private var mLastHeight = 0
    private var mPicker: LengthPicker? = null
    private var mPrefs: SharedPreferences? = null

    init {
        positiveButtonText = context.getText(R.string.height_pref_set)
        negativeButtonText = context.getText(R.string.height_pref_cancel)
    }

    override fun onCreateDialogView(): View {
        val picker = LengthPicker(context)
        picker.gravity = Gravity.CENTER
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        mPicker = picker
        return picker
    }

    override fun onBindDialogView(v: View) {
        super.onBindDialogView(v)

        val minHeight = context.resources.getInteger(R.integer.min_human_height)
        val maxHeight = context.resources.getInteger(R.integer.max_human_height)

        val picker = v as LengthPicker
        picker.setMetricPrecision(10)
        picker.minValue = minHeight
        picker.maxValue = maxHeight
        picker.setUnitLabel(context.getString(R.string.unit_centimeters_short))

        val heightPrefKey = context.getString(R.string.height_pref_key)

        mLastHeight = mPrefs!!.getInt(heightPrefKey, 0)

        if (mLastHeight == 0) {
            val weight = WeightLab.getInstance(context).getLastWeight()
            if (weight != 0) {
                // calculate height for the last weight and the ideal Bmi
                val bmi = context.resources.getFraction(R.fraction.optimal_bmi, 1, 1).toDouble()

                // ToDo: move to utils/Bmi class and make static
                mLastHeight = ((Math.sqrt(weight * 1000 / bmi) + 5) / 10).toInt() * 10

                if (mLastHeight > maxHeight) {
                    mLastHeight = maxHeight
                }

                if (mLastHeight < minHeight) {
                    mLastHeight = minHeight
                }

            } else {
                mLastHeight = context.resources.getInteger(R.integer.average_human_height)
            }
        }
        picker.value = mLastHeight
        mPicker = picker
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        super.onDialogClosed(positiveResult)

        if (positiveResult) {
            if (callChangeListener(mPicker!!.value)) {

                // needed when user edits the text field and clicks OK
                mPicker!!.clearFocus()

                mLastHeight = mPicker!!.value
                persistInt(mLastHeight)
            }
        } else if (mPrefs!!.getInt(context.getString(R.string.height_pref_key), 0) == 0) {
            // Bmi preference cannot be set if weight is not provided
            mPrefs!!.edit().putBoolean(context.getString(R.string.bmi_pref_key), false).apply()
        }
    }

    override fun onSetInitialValue(restoreValue: Boolean, defaultValue: Any) {
        mLastHeight = if (restoreValue) getPersistedInt(mLastHeight) else defaultValue as Int
    }
}
