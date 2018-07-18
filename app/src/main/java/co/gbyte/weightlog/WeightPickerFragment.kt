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

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var weight = arguments?.getInt(ARG_WEIGHT)  ?: 0

        val v = LayoutInflater.from(context).inflate(R.layout.dialog_weight, null)
        val weightPicker = v.findViewById<WeightPicker>(R.id.dialog_weight_weight_picker)

        weightPicker.value = weight
        weightPicker!!.onValueChangedListener =
                WeightPicker.OnValueChangedListener {
            picker, oldValue, newValue -> weight = weightPicker.value
            arguments?.putInt(EXTRA_WEIGHT, weight)
        }

        weightPicker.clearFocus()
        return AlertDialog.Builder(activity)
                .setView(v)
                .setTitle(R.string.weight_picker_title)
                .setNegativeButton(android.R.string.cancel)
                        { dialog, whichButton -> sendResult(Activity.RESULT_CANCELED, weight) }
                .setPositiveButton(android.R.string.ok
                ) { dialogInterface, i ->
                    // needed when user edits the text field and clicks OK:
                    weightPicker.clearFocus()
                    sendResult(Activity.RESULT_OK, weight)
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
