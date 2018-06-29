package co.gbyte.weightlog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater

import java.util.UUID

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import co.gbyte.weightlog.model.WeightLab
import kotlinx.android.synthetic.main.dialog_confirm_delete.view.*

class ConfirmDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val id = arguments?.getSerializable(ARG_WEIGHT_ID) as UUID
        val weight = WeightLab.get(activity).getWeight(id)
        val v = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_confirm_delete, null)

        val weightTextView = v.dialog_confirm_delete_weight_weight
        weightTextView.text = weight!!.weightStringKg

        val dateTextView = v.dialog_confirm_delete_weight_date
        dateTextView.text = DateFormat.getDateFormat(activity).format(weight.time)

        val timeTextView = v.dialog_confirm_delete_weight_time
        timeTextView.text = DateFormat.getTimeFormat(activity).format(weight.time)

        return AlertDialog.Builder(activity as Activity)
                .setView(v)
                .setTitle(R.string.confirm_delete_title)
                .setPositiveButton(android.R.string.ok
                ) { dialogInterface, i ->
                    WeightLab.get(activity).deleteWeight(id)
                    val intent = Intent(activity, MainPagerActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()
    }

    companion object {

        private const val ARG_WEIGHT_ID = "weightId"

        fun newInstance(id: UUID): ConfirmDialogFragment {
            val args = Bundle()
            args.putSerializable(ARG_WEIGHT_ID, id)

            val fragment = ConfirmDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
