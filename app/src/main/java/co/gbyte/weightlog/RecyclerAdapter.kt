package co.gbyte.weightlog

import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.gbyte.weightlog.model.Weight
import co.gbyte.weightlog.utils.Bmi
import co.gbyte.weightlog.utils.Time

import kotlinx.android.synthetic.main.list_item_weight.view.*
import java.util.*

class RecyclerAdapter(
        private val weights: ArrayList<Weight>, private var selectedPosition: Int = -1) :
        RecyclerView.Adapter<RecyclerAdapter.WeightHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            RecyclerAdapter.WeightHolder{
        val inflatedView = parent.inflate(R.layout.list_item_weight, false)
        return WeightHolder(inflatedView)
    }

    override fun getItemCount(): Int = weights.size

    override fun onBindViewHolder(holder: RecyclerAdapter.WeightHolder, position: Int) {
        var weight: Weight? = weights[position]
        val diff: Double? = if (position < weights.size - 1) {
                val prevWeight = weights[position + 1]
                (weight!!.weight - prevWeight.weight).toDouble() / 1000.0
            } else {
                null
            }
        val compactLayout = holder.itemView.list_item_compact
        val extendedLayout = holder.itemView.list_item_extended
        val context = holder.itemView.context
        if (weight != null) {
            Bmi.updateAssessmentView(context,
                    holder.itemView,
                    R.id.assessment_compact_layout,
                    R.id.bmi_compact_tv,
                    weight.bmi(),
                    false)
        }

        if (selectedPosition == position) {
            compactLayout.visibility = View.GONE
            extendedLayout.visibility = View.VISIBLE

            if (weight != null) {
                Bmi.updateAssessmentView(context,
                        holder.itemView,
                        R.id.assessment_extended_layout,
                        R.id.bmi_extended_tv,
                        weight.bmi(),
                        true)
            }
        } else {
            compactLayout.visibility = View.VISIBLE
            extendedLayout.visibility = View.GONE
        }
        holder.itemView.isSelected = selectedPosition == position
        weight = if (selectedPosition == -1) null else weights[selectedPosition]

        if (weight != null) {
            holder.bindWeight(weight, diff)
        }

        holder.itemView.setOnClickListener { view ->
            if (view.isSelected) {
                notifyItemChanged(selectedPosition)
                selectedPosition = -1
                weight = null
                notifyItemChanged(selectedPosition)
            } else {
                notifyItemChanged(selectedPosition)
                selectedPosition = position
                notifyItemChanged(selectedPosition)
            }
        }
    }

    class WeightHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var weight: Weight? = null
        private val context = itemView.context

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val showWeightIntent = Intent(context, WeightActivity::class.java)
            showWeightIntent.putExtra(WEIGHT_ID, weight?.id)
            context.startActivity(showWeightIntent)
        }

        companion object {
            private const val WEIGHT_ID = "WEIGHT"
        }

        fun bindWeight(weight: Weight, weightDiff: Double?) {
            this.weight = weight
            val weightTime = weight.time
            view.weight_date_compact_tv.text = Time.getDateString(context, "", weightTime)
            view.weight_date_extended_tv.text = Time.getShortDateString(context, weightTime)
            view.weight_time_extended_tv.text = Time.getTimeString(context, weightTime)

            view.weight_compact_tv.text = weight.weightStringKg
            view.weight_extended_tv.text = weight.weightStringKg

            val note = weight.note
            if (note == null || note.isEmpty()) {
                view.compat_note_icon?.setImageBitmap(null)
                view.weight_note_extended_tv.visibility = View.GONE
            } else {
                view.compat_note_icon?.setImageResource(R.drawable.ic_icon_note_dark)
                view.weight_note_extended_tv.text = note
                view.weight_note_extended_tv.visibility = View.VISIBLE
            }

            if (weightDiff != null) {
                view.weight_log_extended_change_view.visibility = View.VISIBLE
                when {
                    weightDiff < 0 -> {
                        view.weight_change_compact_tv?.text =
                                String.format(Locale.getDefault(), "%.1f", weightDiff)
                        view.weight_change_compact_tv?.setTextColor(ContextCompat.getColor(
                                context, R.color.colorWeightLoss))
                        view.weight_log_extended_change_tv?.text =
                                String.format(Locale.getDefault(), "%.1f", weightDiff)
                        view.weight_log_extended_change_tv?.setTextColor(ContextCompat.getColor(
                                context, R.color.colorWeightLoss))
                        view.weight_compact_tv?.setTextColor(ContextCompat.getColor(
                                context, R.color.colorWeightLossDark))
                        view.weight_extended_tv?.setTextColor(ContextCompat.getColor(
                                context, R.color.colorWeightLossDark))
                    }
                    weightDiff > 0 -> {
                        view.weight_change_compact_tv?.text =
                                String.format(Locale.getDefault(), "+%.1f", weightDiff)
                        view.weight_change_compact_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorWeightGain))
                        view.weight_log_extended_change_tv?.text =
                                String.format(Locale.getDefault(), "+%.1f", weightDiff)
                        view.weight_log_extended_change_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorWeightGain))
                        view.weight_compact_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorWeightGainDark))
                        view.weight_extended_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorWeightGainDark))
                    }
                    else -> {
                        view.weight_change_compact_tv?.text =
                                String.format(Locale.getDefault(), "%.1f", weightDiff)
                        view.weight_change_compact_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorSecondaryText))
                        view.weight_log_extended_change_tv?.text =
                                String.format(Locale.getDefault(), "%.1f", weightDiff)
                        view.weight_log_extended_change_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorSecondaryText))
                        view.weight_compact_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorSecondaryText))
                        view.weight_extended_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorSecondaryText))
                    }
                }
            } else {
                view.weight_change_compact_tv?.text = ""
                view.weight_log_extended_change_view?.visibility = View.GONE
                view.weight_compact_tv?.setTextColor(
                        ContextCompat.getColor(context, R.color.colorPrimaryText))
                view.weight_extended_tv?.setTextColor(
                        ContextCompat.getColor(context, R.color.colorPrimaryText))
            }
            // ToDo:
            // somehow update menu: LogFragment.updateMenu()
        }
    }
}