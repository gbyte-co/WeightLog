package co.gbyte.weightlog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TimePicker

import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment

class TimePickerFragment : DialogFragment() {

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val time = arguments?.getSerializable(ARG_TIME) as Date

        val calendar = Calendar.getInstance()
        calendar.time = time
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        @SuppressLint("InflateParams")
        val v = LayoutInflater.from(activity).inflate(R.layout.dialog_time, null)

        val timePicker = v.findViewById<TimePicker>(R.id.dialog_time_time_picker)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = hour
            timePicker.minute = minute
        } else {
            @Suppress("DEPRECATION")
            timePicker.currentHour = hour
            @Suppress("DEPRECATION")
            timePicker.currentMinute = minute
        }

        return AlertDialog.Builder(activity)
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok
                ) { _, _ ->
                    val minutesInHour = 60
                    val msInMinute = 60000
                    val newTime = GregorianCalendar(year, month, day).time
                    val lHour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        timePicker.hour
                    } else {
                        @Suppress("DEPRECATION")
                        timePicker.currentHour
                    }
                    val lMinute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        timePicker.minute
                    } else {
                        @Suppress("DEPRECATION")
                        timePicker.currentMinute
                    }
                    val timeMs = newTime.time + (lHour * minutesInHour + lMinute) * msInMinute
                    newTime.time = timeMs
                    sendResult(Activity.RESULT_OK, newTime)
                }
                .create()
    }

    private fun sendResult(resultCode: Int, time: Date) {

        if (targetFragment == null) {
            return
        }

        val intent = Intent()
        intent.putExtra(EXTRA_TIME, time)

        targetFragment?.onActivityResult(targetRequestCode, resultCode, intent)
    }

    companion object {
        const val EXTRA_TIME = "co.gbyte.android.weightlog.time"
        private const val ARG_TIME = "time"

        fun newInstance(time: Date): TimePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_TIME, time)

            val fragment = TimePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
