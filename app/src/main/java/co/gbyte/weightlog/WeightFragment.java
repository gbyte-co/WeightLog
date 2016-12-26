package co.gbyte.weightlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import co.gbyte.weightlog.model.Weight;
import co.gbyte.weightlog.model.WeightLab;

/**
 * Created by walt on 18/10/16.
 *
 */

public class WeightFragment extends Fragment {

    private static final String ARG_WEIGHT_ID  = "weight_id";
    private static final String DIALOG_DATE    = "DialogDate";
    private static final String DIALOG_TIME    = "DialogTime";
    private static final String DIALOG_WEIGHT  = "DialogWeight";
    private static final String DIALOG_CONFIRM = "DialogConfirm";

    private static final int REQUEST_DATE   = 0;
    private static final int REQUEST_TIME   = 1;
    private static final int REQUEST_WEIGHT = 2;

    private Weight mWeight = null;
    private boolean mIsNew = false;
    private Button mDateButton = null;
    private Button mTimeButton = null;
    private Button mWeightButton = null;
    private View mView = null;

    public static WeightFragment newInstance() {
        return new WeightFragment();
    }

    public static WeightFragment newInstance(UUID weightId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEIGHT_ID, weightId);

        WeightFragment fragment = new WeightFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mIsNew = (getArguments() == null);
        if (mIsNew) {
            // new entry:
            mWeight = new Weight();
            int weight = WeightLab.get(getActivity()).getLastWeight();

            if (weight != 0) {
                // start with most recently taken weight
                mWeight.setWeight(weight);
            } else {
                Context context = getActivity();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String heightPrefKey = getString(R.string.height_pref_key);
                // get height if stored in shared preferences or get average if not
                int height = prefs.getInt(
                        heightPrefKey, getResources().getInteger(R.integer.average_human_height));
                // propose weight calculated from *optimal* BMI
                double bmi = getResources().getFraction(R.fraction.optimal_bmi, 1, 1);
                // weight is stored in grams, round it to hundreds
                // ToDo: tested only manually
                weight =  (int) ((bmi * height * height / 1000 + 50) / 100) * 100;

                mWeight.setWeight(weight);
            }

        } else {
            // picked an existing weight
            UUID weightId = (UUID) getArguments().getSerializable(ARG_WEIGHT_ID);
            mWeight = WeightLab.get(getActivity()).getWeight(weightId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_weight, container, false);
        mDateButton = (Button) mView.findViewById(R.id.weight_date_button);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mWeight.getTime());
                dialog.setTargetFragment(WeightFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mTimeButton = (Button) mView.findViewById(R.id.weight_time_button);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mWeight.getTime());
                dialog.setTargetFragment(WeightFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mWeightButton = (Button) mView.findViewById(R.id.weight_button);
        mWeightButton.setText(mWeight.getWeightStringKg());
        mWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                WeightPickerFragment dialog = WeightPickerFragment.newInstance(mWeight.getWeight());
                dialog.setTargetFragment(WeightFragment.this, REQUEST_WEIGHT);
                dialog.show(manager, DIALOG_WEIGHT);
            }
        });

        EditText noteField = (EditText) mView.findViewById(R.id.weight_note);
        noteField.setText(mWeight.getNote());
        noteField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mWeight.setNote(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateAssessmentView(mView);

        return mView;
    }

    private void updateAssessmentView(View v) {
        LinearLayout assessmentLayout = (LinearLayout) v.findViewById(R.id.assessment_layout);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean bmiIsSet = settings.getBoolean(getString(R.string.bmi_pref_key), false);

        if (bmiIsSet) {
            int height = settings.getInt(getString(R.string.height_pref_key), 0);
            Weight.setHeight(height);
            TextView bmiTv = (TextView) v.findViewById(R.id.bmi_tv);
            TextView assessmentTv = (TextView) v.findViewById(R.id.assessment_tv);
            double bmi = mWeight.bmi();

            String assessment;
            int color;

            if (bmi >= getResources().getFraction(R.fraction.overweight_bmi, 1, 1)) {
                if (bmi < getResources().getFraction(R.fraction.moderately_obese_bmi, 1, 1)) {
                    assessment = getString(R.string.overweight);
                    color = ContextCompat.getColor(getContext(), R.color.colorOverweight);

                } else if (bmi < getResources().getFraction(R.fraction.severely_obese_bmi, 1, 1)) {
                    assessment = getString(R.string.moderately_obese);
                    color = ContextCompat.getColor(getContext(), R.color.colorModeratelyObese);
                } else if (bmi < getResources().getFraction(R.fraction.very_severely_obese_bmi,
                                                            1, 1)) {
                    assessment = getString(R.string.severely_obese);
                    color = ContextCompat.getColor(getContext(), R.color.colorSeverelyObese);
                } else {
                    assessment = getString(R.string.very_severely_obese);
                    color = ContextCompat.getColor(getContext(), R.color.colorVerySeverelyObese);
                }
            } else if (bmi < getResources().getFraction(R.fraction.underweight_bmi, 1, 1)) {
                if (bmi > getResources().getFraction(R.fraction.severely_underweight_bmi, 1, 1)) {
                    assessment = getString(R.string.underweight);
                    color = ContextCompat.getColor(getContext(), R.color.colorUnderweight);
                } else if (bmi > getResources().getFraction(R.fraction.
                                                            very_severely_underweight_bmi, 1, 1)) {
                    assessment = getString(R.string.severely_underweight);
                    color = ContextCompat.getColor(getContext(), R.color.colorSeverelyUnderweight);
                } else {
                    assessment = getString(R.string.very_severely_underweight);
                    color = ContextCompat.getColor(getContext(),
                                                   R.color.colorVerySeverelyUnderweight);
                }
            } else {
                assessment = getString(R.string.healthy_weight);
                color = ContextCompat.getColor(getContext(), R.color.colorHealthyWeight);
            }

            bmiTv.setText(String.format(Locale.getDefault(), " %.2f - ",  bmi));
            assessmentTv.setText(assessment);
            assessmentTv.setTextColor(color);
            assessmentLayout.setVisibility(View.VISIBLE);
        } else {
            assessmentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Don't want this:
        //WeightLab.get(getActivity()).updateWeight(mWeight);
        // ToDo: Save instance state instead!!!
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_weight_menu, menu);
        if(mIsNew) {
            menu.getItem(0).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_weight:
                FragmentManager manager = getFragmentManager();
                ConfirmDialogFragment dialog = ConfirmDialogFragment.newInstance(mWeight.getId());
                dialog.show(manager, DIALOG_CONFIRM);
                return true;

            case R.id.menu_item_cancel_weight:
                goBackToList();
                return true;

            case R.id.menu_item_accept_weight:
                if(mIsNew) {
                    WeightLab.get(getActivity()).addWeight(mWeight);
                } else {
                    WeightLab.get(getActivity()).updateWeight(mWeight);
                }
                goBackToList();
                return true;

            /* ... I am not sure yet:
            case R.id.menu_item_settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            */

            default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void goBackToList() {
        Intent intent = new Intent(getActivity(), LogActivity.class);
        // Set the new task and clear flags in order to prevent previous activities from
        // being kept in a queue (stack of activities) - kill all the previous activities:
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date)  data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mWeight.setTime(date);
            updateDate();
        }

        if (requestCode == REQUEST_TIME) {
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mWeight.setTime(time);
            updateTime();
        }

        if (requestCode == REQUEST_WEIGHT) {
            mWeight.setWeight(data.getIntExtra(WeightPickerFragment.EXTRA_WEIGHT, 0));
            mWeightButton.setText(mWeight.getWeightStringKg());
            updateAssessmentView(mView);
        }
    }

    private void updateDate() {
        String dateString =
                new SimpleDateFormat("EEE, ", Locale.getDefault()).format(mWeight.getTime())
                + DateFormat.getMediumDateFormat(getActivity()).format(mWeight.getTime());
        mDateButton.setText(dateString);
    }

    private void updateTime() {
        String timeString = DateFormat.getTimeFormat(getActivity()).format(mWeight.getTime());
        mTimeButton.setText(timeString);
    }
}
