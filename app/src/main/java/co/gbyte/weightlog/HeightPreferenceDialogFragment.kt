package co.gbyte.weightlog

import androidx.preference.PreferenceDialogFragmentCompat
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceManager
import co.gbyte.android.lengthpicker.LengthPicker
import co.gbyte.weightlog.model.WeightLab

class HeightPreferenceDialogFragment : PreferenceDialogFragmentCompat() {

    var mPicker: LengthPicker? = null

    companion object {
        fun newInstance(key: String): HeightPreferenceDialogFragment {
            val fragment = HeightPreferenceDialogFragment()
            val b = Bundle(1)
            b.putString(PreferenceDialogFragmentCompat.ARG_KEY, key)
            fragment.arguments = b

            return fragment
        }
    }

    override fun onBindDialogView(v: View) {
        super.onBindDialogView(v)

        val minHeight = resources.getInteger(R.integer.min_human_height)
        val maxHeight = resources.getInteger(R.integer.max_human_height)

        val picker = v as LengthPicker
        picker.setMetricPrecision(10)
        picker.minValue = minHeight
        picker.maxValue = maxHeight
        picker.setUnitLabel(getString(R.string.unit_centimeters_short))

        val heightPrefKey = getString(R.string.height_pref_key)

        var lastHeight
                = PreferenceManager.getDefaultSharedPreferences(context).getInt(heightPrefKey, 0)

        if (lastHeight == 0) {
            val weight = WeightLab.getInstance(context).getLastWeight()
            if (weight != 0) {
                // calculate height for the last weight and the ideal Bmi
                val bmi = resources.getFraction(R.fraction.optimal_bmi, 1, 1).toDouble()

                // ToDo: move to utils/Bmi class and make static
                lastHeight = ((Math.sqrt(weight * 1000 / bmi) + 5) / 10).toInt() * 10

                if (lastHeight > maxHeight) {
                    lastHeight = maxHeight
                }

                if (lastHeight < minHeight) {
                    lastHeight = minHeight
                }

            } else {
                lastHeight = resources.getInteger(R.integer.average_human_height)
            }
        }
        picker.value = lastHeight
        mPicker = picker

    }

    override fun onDialogClosed(positiveResult: Boolean) {
        /* ToDo:
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
        */
    }
}