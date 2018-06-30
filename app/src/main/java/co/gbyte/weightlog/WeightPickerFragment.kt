package co.gbyte.weightlog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater

import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import co.gbyte.android.weightpicker.WeightPicker

class WeightPickerFragment : DialogFragment() {
    private var mWeight: Int = 0
    private var mWeightPicker: WeightPicker? = null

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mWeight = arguments!!.getInt(ARG_WEIGHT)

        val v = LayoutInflater.from(context)
                .inflate(R.layout.dialog_weight, null)

        mWeightPicker = v.findViewById(R.id.dialog_weight_weight_picker)
        mWeightPicker!!.value = mWeight
        mWeightPicker!!.onValueChangedListener =
                WeightPicker.OnValueChangedListener {
            picker, oldValue, newValue -> mWeight = mWeightPicker!!.value
            arguments!!.putInt(EXTRA_WEIGHT, mWeight)
        }

        mWeightPicker!!.clearFocus()
        return AlertDialog.Builder(activity)
                .setView(v)
                .setTitle(R.string.weight_picker_title)
                .setNegativeButton(android.R.string.cancel)
                        { dialog, whichButton -> sendResult(Activity.RESULT_CANCELED, mWeight) }
                .setPositiveButton(android.R.string.ok
                ) { dialogInterface, i ->
                    // needed when user edits the text field and clicks OK:
                    mWeightPicker!!.clearFocus()
                    sendResult(Activity.RESULT_OK, mWeight)
                }
                .create()
    }

    private fun sendResult(resultCode: Int, weight: Int) {
        if (targetFragment == null) {
            return
        }

        val intent = Intent()
        intent.putExtra(EXTRA_WEIGHT, weight)

        targetFragment!!.onActivityResult(targetRequestCode, resultCode, intent)
    }

    companion object {
        val EXTRA_WEIGHT = "co.gbyte.android.weightlog.weight"
        private val ARG_WEIGHT = "weight"

        fun newInstance(weight: Int): WeightPickerFragment {
            val args = Bundle()
            args.putInt(ARG_WEIGHT, weight)

            val fragment = WeightPickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
