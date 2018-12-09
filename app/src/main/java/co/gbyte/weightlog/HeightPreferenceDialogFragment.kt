package co.gbyte.weightlog

import androidx.preference.PreferenceDialogFragmentCompat
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.preference.DialogPreference
import androidx.preference.PreferenceManager
import co.gbyte.heightpicker.HeightPicker
import co.gbyte.weightlog.model.WeightLab

class HeightPreferenceDialogFragment : PreferenceDialogFragmentCompat() {

    private var mPicker: HeightPicker? = null
    //private var mPicker: LengthPicker? = null
    private var averageHumanHeight = 0

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
        averageHumanHeight = resources.getInteger(R.integer.average_human_height)

        val picker = v as HeightPicker
        picker.metricPrecision = 10
        picker.minValue = minHeight
        picker.maxValue = maxHeight
        picker.value = averageHumanHeight
        picker.unitsLabel = getString(R.string.unit_centimeters_short)


        val heightPrefKey = getString(R.string.height_pref_key)
        var height = PreferenceManager.getDefaultSharedPreferences(context).getInt(heightPrefKey, 0)

        if (height == 0) {
            val weight = WeightLab.getInstance(context).getLastWeight()
            if (weight != 0) {
                // calculate height for the last weight and the ideal Bmi
                val bmi = resources.getFraction(R.fraction.optimal_bmi, 1, 1).toDouble()

                // ToDo: move to utils/Bmi class and make static
                height = ((Math.sqrt(weight * 1000 / bmi) + 5) / 10).toInt() * 10

                if (height > maxHeight) {
                    height = maxHeight
                }

                if (height < minHeight) {
                    height = minHeight
                }

            } else {
                height = averageHumanHeight
            }
        }
        picker.value = height
        mPicker = picker
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val heightPreference: DialogPreference = preference
        if (positiveResult) {
            val newHeight = mPicker?.value ?: averageHumanHeight
            if (heightPreference is HeightPreference) {
                if (preference.callChangeListener(mPicker?.value)) {

                    // needed when user edits the text field and clicks OK
                    // ToDo: or is it?
                    mPicker?.clearFocus()
                    heightPreference.height = newHeight
                    // ToDo: make it work:
                    //Settings.height = newHeight
                }
                Toast.makeText(context,"Height set to ${heightPreference.height / 10} cm",
                        Toast.LENGTH_LONG).show()
            }
        } else if (prefs.getInt(context?.getString(R.string.height_pref_key), 0) == 0) {
            // Bmi preference cannot be set if weight is not provided
            prefs.edit().putBoolean(context?.getString(R.string.bmi_pref_key), false).apply()
            // ToDo: make it work:
            //Settings.showBmi = false
        }
    }
}