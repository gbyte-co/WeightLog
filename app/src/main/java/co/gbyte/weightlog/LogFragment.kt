package co.gbyte.weightlog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.gbyte.weightlog.R.id.list_item_compact
import co.gbyte.weightlog.R.id.weight_recycler_view

import kotlinx.android.synthetic.main.list_item_weight.*
import kotlinx.android.synthetic.main.list_item_weight.view.*

import java.util.Locale

import co.gbyte.weightlog.model.Weight
import co.gbyte.weightlog.model.WeightLab
import co.gbyte.weightlog.utils.Bmi
import co.gbyte.weightlog.utils.Time
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_weight_log.*

class LogFragment : Fragment() {

    private var mWeightRecycleView: RecyclerView? = null
    private var mContext: Context? = null
    private var mWeight: Weight? = null
    private var mMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mContext = activity

        return inflater.inflate(R.layout.fragment_weight_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //mWeightRecycleView!!.layoutManager = LinearLayoutManager(mContext)


        //val addFab:FloatingActionButton? = view.findViewById(R.id.fab_new_weight) as FloatingActionButton?
        /*
        val addFab: FloatingActionButton? = fab_new_weight
        addFab?.setOnClickListener {
            val intent = WeightActivity.newIntent(mContext)
            startActivity(intent)
        }
        */

        val settings = PreferenceManager.getDefaultSharedPreferences(mContext)
        if (!settings.contains(getString(R.string.bmi_pref_key))) {
            // The app is running for the first time. Ask user for basic settings.
            showSettings()
        }

        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_log_menu, menu)
        mMenu = menu
        updateMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_item_settings -> {
                showSettings()
                return true
            }
            R.id.menu_item_edit_weight -> {
                if (mWeight != null) {
                    val intent = WeightActivity.newIntent(mContext, mWeight!!.id)
                    startActivity(intent)
                }
                return true
            }
            R.id.menu_item_test -> {
                val testDialog = TestDialog()
                testDialog.show(activity!!.fragmentManager, "Test Dialog")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showSettings() {
        val intent = Intent(mContext, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun updateUI() {
        val weightLab = WeightLab.get(mContext)
        val weights = weightLab.weights

        var adapter: WeightAdapter? = null
        if (adapter == null) {
            adapter = WeightAdapter(weights)
            // ToDo: fix this:
            //mWeightRecycleView!!.adapter = adapter
        } else {
            adapter.setWeights(weights)
            adapter.notifyDataSetChanged()
            // ToDo: Don't forget to switch to:
            //adapter.notifyItemChanged(<position>);
            //
            // Note:
            // The challenge is discovering which position has
            // changed and reloading the correct item.
        }
    }

    private fun updateMenu() {
        val editMenuItem = mMenu!!.findItem(R.id.menu_item_edit_weight)
        editMenuItem.isVisible = mWeight != null
    }

    private inner class WeightHolder internal constructor(itemView: View)
            :RecyclerView.ViewHolder(itemView) {

        /*
        internal var mDateCompactTV: TextView
        internal var mDateExtendedTV: TextView
        //TextView mTimeCompactTV;
        internal var mTimeExtendedTV: TextView

        internal var mWeightCompactTV: TextView
        internal var mWeightExtendedTV: TextView

        internal var mWeightChangeCompactTV: TextView
        internal var mWeightChangeExtendedTV: TextView

        internal var mCompactLayout: RelativeLayout
        internal var mExtendedLayout: LinearLayout

        internal var mExtendedWeightChangeView: LinearLayout

        internal var mCompactNoteIcon: ImageView
        internal var mExtendedNote: TextView

        init {
            mCompactLayout = list_item_compact
            mExtendedLayout = list_item_extended

            mDateCompactTV = weight_date_compact_tv
            mDateExtendedTV = weight_date_extended_tv

            mTimeExtendedTV = weight_time_extended_tv

            mWeightCompactTV = weight_compact_tv
            mWeightExtendedTV = weight_extended_tv

            mWeightChangeCompactTV = weight_change_compact_tv
            mWeightChangeExtendedTV = weight_log_extended_change_tv

            mExtendedWeightChangeView = weight_log_extended_change_view

            mCompactNoteIcon = compat_note_icon
            mExtendedNote = weight_note_extended_tv
        }

        internal fun bindWeight(weight: Weight, weightChange: Double?) {

            val weightTime = weight.time
            mDateCompactTV.text = Time.getDateString(mContext, "", weightTime)
            mDateExtendedTV.text = Time.getShortDateString(mContext, weightTime)

            val time = Time.getTimeString(mContext, weightTime)
            //mTimeCompactTV.setText(time);
            mTimeExtendedTV.text = time

            mWeightCompactTV.text = weight.weightStringKg
            mWeightExtendedTV.text = weight.weightStringKg

            val note = weight.note
            if (note == null || note.isEmpty()) {
                mCompactNoteIcon.setImageBitmap(null)
                mExtendedNote.visibility = View.GONE
            } else {
                mCompactNoteIcon.setImageResource(R.drawable.ic_icon_note_dark)
                mExtendedNote.text = note
                mExtendedNote.visibility = View.VISIBLE
            }

            if (weightChange != null) {
                mExtendedWeightChangeView.visibility = View.VISIBLE
                when {
                    weightChange < 0 -> {
                        mWeightChangeCompactTV.text =
                                String.format(Locale.getDefault(), "%.1f", weightChange)
                        mWeightChangeCompactTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorWeightLoss))
                        mWeightChangeExtendedTV.text =
                                String.format(Locale.getDefault(), "%.1f", weightChange)
                        mWeightChangeExtendedTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorWeightLoss))
                        mWeightCompactTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorWeightLossDark))
                        mWeightExtendedTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorWeightLossDark))
                    }
                    weightChange > 0 -> {
                        mWeightChangeCompactTV.text =
                                String.format(Locale.getDefault(), "+%.1f", weightChange)
                        mWeightChangeCompactTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorWeightGain))
                        mWeightChangeExtendedTV.text =
                                String.format(Locale.getDefault(), "+%.1f", weightChange)
                        mWeightChangeExtendedTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorWeightGain))
                        mWeightCompactTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorWeightGainDark))
                        mWeightExtendedTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorWeightGainDark))
                    }
                    else -> {
                        mWeightChangeCompactTV.text =
                                String.format(Locale.getDefault(), "%.1f", weightChange)
                        mWeightChangeCompactTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorSecondaryText))
                        mWeightChangeExtendedTV.text =
                                String.format(Locale.getDefault(), "%.1f", weightChange)
                        mWeightChangeExtendedTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorSecondaryText))
                        mWeightCompactTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorSecondaryText))
                        mWeightExtendedTV.setTextColor(ContextCompat.getColor(context!!,
                                R.color.colorSecondaryText))
                    }
                }
            } else {
                mWeightChangeCompactTV.text = ""
                mExtendedWeightChangeView.visibility = View.GONE
                mWeightCompactTV.setTextColor(ContextCompat.getColor(context!!,
                        R.color.colorPrimaryText))
                mWeightExtendedTV.setTextColor(ContextCompat.getColor(context!!,
                        R.color.colorPrimaryText))
            }

        }
        */
    }

    private inner class WeightAdapter internal constructor(private var mWeights: List<Weight>?)
            : RecyclerView.Adapter<WeightHolder>() {
        private var mSelectedPos = -1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightHolder {
            val layoutInflater = LayoutInflater.from(mContext)
            val view =
                    layoutInflater.inflate(R.layout.list_item_weight, parent, false)

            return WeightHolder(view)
        }

        override fun onBindViewHolder(holder: WeightHolder, position: Int) {
            val weight = mWeights!![position]
            val difference: Double? = if (position < mWeights!!.size - 1) {
                val prevWeight = mWeights!![position + 1]
                (weight.weight - prevWeight.weight).toDouble() / 1000.0
            } else {
                null
            }

            /*
            val compactLayout = holder.itemView.list_item_compact
            val extendedLayout = holder.itemView.list_item_extended
            Bmi.updateAssessmentView(mContext!!,
                    holder.itemView,
                    R.id.assessment_compact_layout,
                    R.id.bmi_compact_tv,
                    weight.bmi(),
                    false)
            Bmi.updateAssessmentView(mContext!!,
                    holder.itemView,
                    R.id.assessment_compact_layout,
                    R.id.bmi_compact_tv,
                    weight.bmi(),
                    false)

            if (mSelectedPos == position) {
                compactLayout.visibility = View.GONE
                extendedLayout.visibility = View.VISIBLE

                Bmi.updateAssessmentView(mContext!!,
                        holder.itemView,
                        R.id.assessment_extended_layout,
                        R.id.bmi_extended_tv,
                        weight.bmi(),
                        true)
                // ToDo: Why do I have to call it twice to make it work ??
                Bmi.updateAssessmentView(mContext!!,
                        holder.itemView,
                        R.id.assessment_extended_layout,
                        R.id.bmi_extended_tv,
                        weight.bmi(),
                        true)
            } else {
                compactLayout.visibility = View.VISIBLE
                extendedLayout.visibility = View.GONE
            }
            holder.itemView.isSelected = mSelectedPos == position
            mWeight = if (mSelectedPos == -1) null else mWeights!![mSelectedPos]

            holder.bindWeight(weight, difference)

            holder.itemView.setOnClickListener { view ->
                if (view.isSelected) {
                    notifyItemChanged(mSelectedPos)
                    mSelectedPos = -1
                    mWeight = null
                    notifyItemChanged(mSelectedPos)
                } else {
                    notifyItemChanged(mSelectedPos)
                    mSelectedPos = position
                    mWeight = weight
                    notifyItemChanged(mSelectedPos)
                }
                updateMenu()
            }
            */
        }

        override fun getItemCount(): Int {
            return mWeights!!.size
        }

        fun setWeights(weights: List<Weight>) {
            mWeights = weights
        }
    }
}
