package co.gbyte.weightlog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker

import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

import androidx.fragment.app.DialogFragment

class DatePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_DATE) as Date

        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        val v = LayoutInflater.from(activity).inflate(R.layout.dialog_date, null)
        val datePicker = v.findViewById<DatePicker>(R.id.dialog_date_date_picker)
        datePicker.init(year, month, day, null)

        return AlertDialog.Builder(activity)
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok
                ) { _, _ ->
                    val minutesInHour = 60
                    val msInMinute = 60000
                    val lYear = datePicker.year
                    val lMonth = datePicker.month
                    val lDay = datePicker.dayOfMonth
                    val lDate = GregorianCalendar(lYear, lMonth, lDay).time
                    lDate.time = lDate.time + (hour * minutesInHour + minute) * msInMinute
                    sendResult(Activity.RESULT_OK, lDate)
                }
                .create()
    }

    private fun sendResult(resultCode: Int, date: Date) {
        if (targetFragment == null) {
            return
        }
        val intent = Intent()
        intent.putExtra(EXTRA_DATE, date)

        targetFragment?.onActivityResult(targetRequestCode, resultCode, intent)
    }

    companion object {
        const val EXTRA_DATE = "co.gbyte.weightlog.date"
        private const val ARG_DATE = "date"

        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)

            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
