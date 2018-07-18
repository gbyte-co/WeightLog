package co.gbyte.weightlog.model

import android.content.Context
import android.preference.PreferenceManager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import java.util.Locale

import androidx.core.content.ContextCompat
import co.gbyte.weightlog.R

object Bmi {

    private fun assessmentString(context: Context, bmi: Double): String {
        val assessment: String
        //if (bmi >= Resources.getSystem().getFraction(R.fraction.overweight_bmi, 1, 1)) {
        if (bmi >= context.resources.getFraction(R.fraction.overweight_bmi, 1, 1)) {
            if (bmi < context.resources.getFraction(R.fraction.moderately_obese_bmi, 1, 1)) {
                assessment = context.resources.getString(R.string.overweight)
            } else if (bmi < context.resources.getFraction(R.fraction.severely_obese_bmi, 1, 1)) {
                assessment = context.resources.getString(R.string.moderately_obese)
            } else if (bmi < context.resources.getFraction(R.fraction.very_severely_obese_bmi,
                            1, 1)) {
                assessment = context.resources.getString(R.string.severely_obese)
            } else {
                assessment = context.resources.getString(R.string.very_severely_obese)
            }
        } else if (bmi < context.resources.getFraction(R.fraction.underweight_bmi, 1, 1)) {
            if (bmi > context.resources.getFraction(R.fraction.severely_underweight_bmi, 1, 1)) {
                assessment = context.resources.getString(R.string.underweight)
            } else if (bmi > context.resources.getFraction(R.fraction.very_severely_underweight_bmi, 1, 1)) {
                assessment = context.resources.getString(R.string.severely_underweight)
            } else {
                assessment = context.resources.getString(R.string.very_severely_underweight)
            }
        } else {
            assessment = context.resources.getString(R.string.healthy_weight)
        }
        return assessment
    }

    private fun assessmentColor(context: Context, bmi: Double): Int {
        val color: Int
        if (bmi >= context.resources.getFraction(R.fraction.overweight_bmi, 1, 1)) {
            if (bmi < context.resources.getFraction(R.fraction.moderately_obese_bmi, 1, 1)) {
                color = ContextCompat.getColor(context, R.color.colorOverweight)
            } else if (bmi < context.resources.getFraction(R.fraction.severely_obese_bmi, 1, 1)) {
                color = ContextCompat.getColor(context, R.color.colorModeratelyObese)
            } else if (bmi < context.resources.getFraction(R.fraction.very_severely_obese_bmi,
                            1, 1)) {
                color = ContextCompat.getColor(context, R.color.colorSeverelyObese)
            } else {
                color = ContextCompat.getColor(context, R.color.colorVerySeverelyObese)
            }
        } else if (bmi < context.resources.getFraction(R.fraction.underweight_bmi, 1, 1)) {
            if (bmi > context.resources.getFraction(R.fraction.severely_underweight_bmi, 1, 1)) {
                color = ContextCompat.getColor(context, R.color.colorUnderweight)
            } else if (bmi > context.resources.getFraction(R.fraction.very_severely_underweight_bmi, 1, 1)) {
                color = ContextCompat.getColor(context, R.color.colorSeverelyUnderweight)
            } else {
                color = ContextCompat.getColor(context, R.color.colorVerySeverelyUnderweight)
            }
        } else {
            color = ContextCompat.getColor(context, R.color.colorHealthyWeight)
        }
        return color
    }

    // ToDo: 'quick and dirty' version for now:
    fun updateAssessmentView(context: Context,
                             parentView: View,
                             layoutResId: Int,
                             bmiResTextViewId: Int,
                             bmi: Double,
                             assessment: Boolean
    ) {
        val layout = parentView.findViewById<LinearLayout>(layoutResId)
        val settings = PreferenceManager.getDefaultSharedPreferences(parentView.context)

        val bmiIsSet = settings.getBoolean(context.resources.getString(R.string.bmi_pref_key), false)

        if (bmiIsSet) {
            val height = settings.getInt(context.resources.getString(R.string.height_pref_key), 0)
            Weight.setHeight(height)
            val bmiTv = parentView.findViewById<TextView>(bmiResTextViewId)

            if (assessment) {
                bmiTv.text = String.format(Locale.getDefault(),
                        " %.2f - %s",
                        bmi,
                        assessmentString(context, bmi))
            } else {
                bmiTv.text = String.format(Locale.getDefault(), "%.2f", bmi)
            }

            bmiTv.setTextColor(assessmentColor(context, bmi))
            layout.visibility = View.VISIBLE
        } else {
            layout.visibility = View.GONE
        }
    }
}
