package co.gbyte.weightlog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import co.gbyte.weightlog.R;
import co.gbyte.weightlog.model.Weight;

/**
 * Created by walt on 16/01/17.
 *
 */

public class Bmi {

    private static String assessmentString (Context context, double bmi) {
        String assessment;
        //if (bmi >= Resources.getSystem().getFraction(R.fraction.overweight_bmi, 1, 1)) {
        if (bmi >= context.getResources().getFraction(R.fraction.overweight_bmi, 1, 1)) {
            if (bmi < context.getResources().getFraction(R.fraction.moderately_obese_bmi, 1, 1)) {
                assessment = context.getResources().getString(R.string.overweight);
            } else if (bmi <
                    context.getResources().getFraction(R.fraction.severely_obese_bmi, 1, 1)) {
                assessment = context.getResources().getString(R.string.moderately_obese);
            } else if (bmi < context.getResources().getFraction(R.fraction.very_severely_obese_bmi,
                    1, 1)) {
                assessment = context.getResources().getString(R.string.severely_obese);
            } else {
                assessment = context.getResources().getString(R.string.very_severely_obese);
            }
        } else if (bmi < context.getResources().getFraction(R.fraction.underweight_bmi, 1, 1)) {
            if (bmi > context.getResources().getFraction(R.fraction.severely_underweight_bmi, 1, 1))
            {
                assessment = context.getResources().getString(R.string.underweight);
            } else if (bmi > context.getResources().getFraction(R.fraction.
                    very_severely_underweight_bmi, 1, 1)) {
                assessment = context.getResources().getString(R.string.severely_underweight);
            } else {
                assessment = context.getResources().getString(R.string.very_severely_underweight);
            }
        } else {
            assessment = context.getResources().getString(R.string.healthy_weight);
        }
        return assessment;
    }

    private static int assessmentColor (Context context, double bmi) {
        int color;
        if (bmi >= context.getResources().getFraction(R.fraction.overweight_bmi, 1, 1)) {
            if (bmi < context.getResources().getFraction(R.fraction.moderately_obese_bmi, 1, 1)) {
                color = ContextCompat.getColor(context, R.color.colorOverweight);
            } else if (bmi <
                    context.getResources().getFraction(R.fraction.severely_obese_bmi, 1, 1)) {
                color = ContextCompat.getColor(context, R.color.colorModeratelyObese);
            } else if (bmi < context.getResources().getFraction(R.fraction.very_severely_obese_bmi,
                    1, 1)) {
                color = ContextCompat.getColor(context, R.color.colorSeverelyObese);
            } else {
                color = ContextCompat.getColor(context, R.color.colorVerySeverelyObese);
            }
        } else if (bmi < context.getResources().getFraction(R.fraction.underweight_bmi, 1, 1)) {
            if (bmi > context.getResources().getFraction(R.fraction.severely_underweight_bmi, 1, 1))
            {
                color = ContextCompat.getColor(context, R.color.colorUnderweight);
            } else if (bmi > context.getResources().getFraction(R.fraction.
                    very_severely_underweight_bmi, 1, 1)) {
                color = ContextCompat.getColor(context, R.color.colorSeverelyUnderweight);
            } else {
                color = ContextCompat.getColor(context, R.color.colorVerySeverelyUnderweight);
            }
        } else {
            color = ContextCompat.getColor(context, R.color.colorHealthyWeight);
        }
        return color;
    }

    public static void updateAssessmentView(Context context,
                                            View parentView,
                                            int layoutResId,
                                            int bmiResTextViewId,
                                            double bmi) {
        LinearLayout layout = (LinearLayout) parentView.findViewById(layoutResId);
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(parentView.getContext());

       boolean bmiIsSet =
               settings.getBoolean(context.getResources().getString(R.string.bmi_pref_key), false);

        if (bmiIsSet) {
            int height =
                    settings.getInt(context.getResources().getString(R.string.height_pref_key), 0);
            Weight.setHeight(height);
            TextView bmiTv = (TextView) parentView.findViewById(bmiResTextViewId);

            bmiTv.setText(String.format(Locale.getDefault(),
                    " %.2f - %s",
                    bmi,
                    assessmentString(context, bmi)));
            bmiTv.setTextColor(Bmi.assessmentColor(context, bmi));
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }
}
