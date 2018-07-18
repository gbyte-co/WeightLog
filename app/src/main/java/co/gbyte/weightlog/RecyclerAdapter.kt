package co.gbyte.weightlog

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.gbyte.weightlog.model.Weight
import co.gbyte.weightlog.model.Bmi

import kotlinx.android.synthetic.main.list_item_weight.view.*
import java.util.*

class RecyclerAdapter(
        private val weights: ArrayList<Weight>, private var selectedPosition: Int = -1) :
        RecyclerView.Adapter<RecyclerAdapter.WeightHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :
            RecyclerAdapter.WeightHolder {
        val inflatedView = parent.inflate(R.layout.list_item_weight, false)
        return WeightHolder(inflatedView)
    }

    override fun getItemCount(): Int = weights.size

    override fun onBindViewHolder(holder: RecyclerAdapter.WeightHolder, position: Int) {

        val weight = weights[position]

        val diff: Double? = if (position < weights.size - 1) {
                    val prevWeight = weights[position + 1]
                    (weight.weight - prevWeight.weight).toDouble() / 1000.0
            } else {
                null
            }
        val compactLayout = holder.itemView.list_item_compact
        val extendedLayout = holder.itemView.list_item_extended
        val context = holder.itemView.context
        Bmi.updateAssessmentView(context,
                holder.itemView,
                R.id.assessment_compact_layout,
                R.id.bmi_compact_tv,
                weight.bmi(),
                false)

    if (selectedPosition == position) {
        compactLayout.visibility = View.GONE
        extendedLayout.visibility = View.VISIBLE

            Bmi.updateAssessmentView(context,
                    holder.itemView,
                    R.id.assessment_extended_layout,
                    R.id.bmi_extended_tv,
                    weight.bmi(),
                    true)
        } else {
            compactLayout.visibility = View.VISIBLE
            extendedLayout.visibility = View.GONE
        }
        holder.itemView.isSelected = (selectedPosition == position)

        holder.bindWeight(weight, diff)

        holder.itemView.setOnClickListener { view ->
            notifyItemChanged(selectedPosition)
            if (view.isSelected) {
                selectedPosition = -1
            } else {
                selectedPosition = position
            }
            notifyItemChanged(selectedPosition)
        }
    }

    class WeightHolder(v: View) : RecyclerView.ViewHolder(v), View.OnLongClickListener {
        private val context = itemView.context
        private var view: View = v
        private var weight: Weight? = null

        init {
            v.setOnLongClickListener(this)
        }

        override fun onLongClick(p0: View?): Boolean {
            if (view.list_item_extended.visibility == View.VISIBLE) {
                val intent = WeightActivity.newIntent(context, weight!!.id)
                val context = itemView.context
                context.startActivity(intent)
            }
            return true
        }

        /*
        companion object {
            private const val WEIGHT_ID = "WEIGHT"
        }
        */

        fun bindWeight(weight: Weight, diff: Double? = null) {
            this.weight = weight
            val weightTime = weight.time
            view.weight_date_compact_tv.text = weightTime.getDateString(context, "")
            view.weight_date_extended_tv.text = weightTime.getShortDateString(context)
            view.weight_time_extended_tv.text =weightTime.getTimeString(context)

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
            if (diff != null) {
                view.weight_log_extended_change_view.visibility = View.VISIBLE
                when {
                    diff < 0 -> {
                        view.weight_change_compact_tv?.text =
                                String.format(Locale.getDefault(), "%.1f", diff)
                        view.weight_change_compact_tv?.setTextColor(ContextCompat.getColor(
                                context, R.color.colorWeightLoss))
                        view.weight_log_extended_change_tv?.text =
                                String.format(Locale.getDefault(), "%.1f", diff)
                        view.weight_log_extended_change_tv?.setTextColor(ContextCompat.getColor(
                                context, R.color.colorWeightLoss))
                        view.weight_compact_tv?.setTextColor(ContextCompat.getColor(
                                context, R.color.colorWeightLossDark))
                        view.weight_extended_tv?.setTextColor(ContextCompat.getColor(
                                context, R.color.colorWeightLossDark))
                    }
                    diff > 0 -> {
                        view.weight_change_compact_tv?.text =
                                String.format(Locale.getDefault(), "+%.1f", diff)
                        view.weight_change_compact_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorWeightGain))
                        view.weight_log_extended_change_tv?.text =
                                String.format(Locale.getDefault(), "+%.1f", diff)
                        view.weight_log_extended_change_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorWeightGain))
                        view.weight_compact_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorWeightGainDark))
                        view.weight_extended_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorWeightGainDark))
                    }
                    else -> {
                        view.weight_change_compact_tv?.text =
                                String.format(Locale.getDefault(), "%.1f", diff)
                        view.weight_change_compact_tv?.setTextColor(
                                ContextCompat.getColor(context, R.color.colorSecondaryText))
                        view.weight_log_extended_change_tv?.text =
                                String.format(Locale.getDefault(), "%.1f", diff)
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
        }
    }
}