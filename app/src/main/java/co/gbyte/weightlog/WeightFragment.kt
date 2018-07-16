package co.gbyte.weightlog

import android.app.Activity
import android.content.Intent
import android.preference.PreferenceManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import java.util.Date
import java.util.UUID

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import co.gbyte.weightlog.model.Weight
import co.gbyte.weightlog.model.WeightLab
import co.gbyte.weightlog.utils.Bmi
import co.gbyte.weightlog.utils.Time

class WeightFragment : Fragment() {

    private var mWeight: Weight? = null
    private var mIsNew = false
    private var mDateButton: Button? = null
    private var mTimeButton: Button? = null
    private var mWeightButton: Button? = null
    private var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mIsNew = arguments == null
        if (mIsNew) {
            // new entry:
            mWeight = Weight()
            var weight = WeightLab.get(activity).lastWeight

            if (weight != 0) {
                // start with most recently taken weight
                mWeight?.weight = weight
            } else {
                val context = activity
                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                val heightPrefKey = getString(R.string.height_pref_key)
                // get height if stored in shared preferences or average human height if not
                val height = prefs.getInt(heightPrefKey, resources.getInteger(R.integer.average_human_height))
                // propose weight calculated from *optimal* Bmi
                val bmi = resources.getFraction(R.fraction.optimal_bmi, 1, 1).toDouble()
                // weight is stored in grams, round it to hundreds
                // ToDo: tested only manually
                weight = ((bmi * height.toDouble() * height.toDouble() / 1000 + 50) / 100).toInt() * 100

                mWeight?.weight = weight
            }

        } else {
            // picked an existing weight
            val weightId = arguments?.getSerializable(ARG_WEIGHT_ID) as UUID
            mWeight = WeightLab.get(activity).getWeight(weightId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        mView = inflater.inflate(R.layout.fragment_weight, container, false)
        mDateButton = mView?.findViewById(R.id.weight_date_button)
        mDateButton?.setOnClickListener {
            val manager = fragmentManager
            val dialog = DatePickerFragment.newInstance(mWeight?.time)
            dialog.setTargetFragment(this@WeightFragment, REQUEST_DATE)
            dialog.show(manager, DIALOG_DATE)
        }

        mTimeButton = mView?.findViewById(R.id.weight_time_button)
        mTimeButton?.setOnClickListener {
            val manager = fragmentManager
            val dialog = TimePickerFragment.newInstance(mWeight!!.time)
            dialog.setTargetFragment(this@WeightFragment, REQUEST_TIME)
            dialog.show(manager, DIALOG_TIME)
        }

        mWeightButton = mView!!.findViewById(R.id.weight_button)
        mWeightButton?.setOnClickListener {
            val manager = fragmentManager
            val dialog = WeightPickerFragment.newInstance(mWeight!!.weight)
            dialog.setTargetFragment(this@WeightFragment, REQUEST_WEIGHT)
            dialog.show(manager, DIALOG_WEIGHT)
        }

        val noteField = mView!!.findViewById<EditText>(R.id.weight_note)
        noteField.setText(mWeight!!.note)
        noteField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mWeight!!.note = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        updateUi()
        return mView as View
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mWeight != null) {
            outState.putLong(WEIGHT_TIME, mWeight!!.time.time)
            outState.putInt(WEIGHT, mWeight!!.weight)
        }
    }

    override fun onViewStateRestored(@Nullable savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            mWeight?.time = Date(savedInstanceState.getLong("date"))
            mWeight?.weight = savedInstanceState.getInt("weight")
            updateUi()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_weight_menu, menu)
        if (mIsNew) {
            menu?.getItem(0)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_item_delete_weight -> {
                val manager = fragmentManager
                val dialog = ConfirmDialogFragment.newInstance(mWeight!!.id)
                dialog.show(manager, DIALOG_CONFIRM)
                return true
            }

            R.id.menu_item_cancel_weight -> {
                goBackToList()
                return true
            }

            R.id.menu_item_accept_weight -> {
                if (mIsNew) {
                    WeightLab.get(activity).addWeight(mWeight)
                } else {
                    WeightLab.get(activity).updateWeight(mWeight)
                }
                goBackToList()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun goBackToList() {
        val intent = Intent(activity, MainPagerActivity::class.java)
        // Set the new task and clear flags in order to prevent previous activities from
        // being kept in a queue (stack of activities) - kill all the previous activities:
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_DATE) {
            val date = data!!.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            mWeight!!.time = date
            mDateButton!!.text = Time.getDateString(context, "EEE ", mWeight!!.time)
            return
        }

        if (requestCode == REQUEST_TIME) {
            val time = data!!.getSerializableExtra(TimePickerFragment.EXTRA_TIME) as Date
            mWeight!!.time = time
            mTimeButton!!.text = Time.getTimeString(context, mWeight!!.time)
            return
        }

        if (requestCode == REQUEST_WEIGHT) {
            mWeight!!.weight = data!!.getIntExtra(WeightPickerFragment.EXTRA_WEIGHT, 0)
            mWeightButton!!.text = mWeight!!.weightStringKg
        }

        updateUi()
    }

    private fun updateUi() {
        mDateButton?.text = Time.getDateString(activity, "EEE, ", mWeight?.time)
        mTimeButton?.text = Time.getTimeString(activity, mWeight?.time)
        mWeightButton?.text = mWeight!!.weightStringKg
        Bmi.updateAssessmentView(activity,
                mView!!,
                R.id.assessment_layout,
                R.id.bmi_tv,
                mWeight!!.bmi(),
                true)
    }

    companion object {

        private const val ARG_WEIGHT_ID = "arg_weight_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val DIALOG_TIME = "DialogTime"
        private const val DIALOG_WEIGHT = "DialogWeight"
        private const val DIALOG_CONFIRM = "DialogConfirm"

        private const val WEIGHT_TIME = "weight_time"
        private const val WEIGHT = "weight"

        private const val REQUEST_DATE = 0
        private const val REQUEST_TIME = 1
        private const val REQUEST_WEIGHT = 2

        fun newInstance(): Fragment {
            return WeightFragment()
        }

        fun newInstance(weightId: UUID): WeightFragment {
            val args = Bundle()
            args.putSerializable(ARG_WEIGHT_ID, weightId)

            val fragment = WeightFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
