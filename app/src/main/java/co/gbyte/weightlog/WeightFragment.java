package co.gbyte.weightlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import co.gbyte.weightlog.model.Weight;
import co.gbyte.weightlog.model.WeightLab;
import co.gbyte.weightlog.utils.Bmi;
import co.gbyte.weightlog.utils.Time;

/**
 * Created by walt on 18/10/16.
 *
 */

public class WeightFragment extends Fragment {

    private static final String ARG_WEIGHT_ID  = "arg_weight_id";
    private static final String DIALOG_DATE    = "DialogDate";
    private static final String DIALOG_TIME    = "DialogTime";
    private static final String DIALOG_WEIGHT  = "DialogWeight";
    private static final String DIALOG_CONFIRM = "DialogConfirm";

    private static final String WEIGHT_TIME    = "weight_time";
    private static final String WEIGHT         = "weight";

    private static final int REQUEST_DATE   = 0;
    private static final int REQUEST_TIME   = 1;
    private static final int REQUEST_WEIGHT = 2;

    private Context mContext;
    private Weight mWeight = null;
    private boolean mIsNew = false;
    private Button mDateButton = null;
    private Button mTimeButton = null;
    private Button mWeightButton = null;
    private View mView = null;

    public static Fragment newInstance() {
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
                // propose weight calculated from *optimal* Bmi
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
        super.onCreateView(inflater, container, savedInstanceState);
        mContext = getActivity();

        mView = inflater.inflate(R.layout.fragment_weight, container, false);
        mDateButton = (Button) mView.findViewById(R.id.weight_date_button);
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
        mWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                WeightPickerFragment dialog = WeightPickerFragment.Companion.newInstance(mWeight.getWeight());
                dialog.setTargetFragment(WeightFragment.this, REQUEST_WEIGHT);
                dialog.show(manager, DIALOG_WEIGHT);
            }
        });

        EditText noteField = (EditText) mView.findViewById(R.id.weight_note);
        noteField.setText(mWeight.getNote());
        noteField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mWeight.setNote(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        updateUi();
        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mWeight != null) {
            outState.putLong(WEIGHT_TIME, mWeight.getTime().getTime());
            outState.putInt(WEIGHT, mWeight.getWeight());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mWeight.setTime(new Date(savedInstanceState.getLong("date")));
            mWeight.setWeight(savedInstanceState.getInt("weight"));
            updateUi();
        }
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
                ConfirmDialogFragment dialog = ConfirmDialogFragment.Companion.newInstance(mWeight.getId());
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
        Intent intent = new Intent(getActivity(), MainPagerActivity.class);
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
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mWeight.setTime(date);
            mDateButton.setText(Time.getDateString(getContext(), "EEE ", mWeight.getTime()));
            return;
        }

        if (requestCode == REQUEST_TIME) {
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mWeight.setTime(time);
            mTimeButton.setText(Time.getTimeString(getContext(), mWeight.getTime()));
            return;
        }

        if (requestCode == REQUEST_WEIGHT) {
            mWeight.setWeight(data.getIntExtra(WeightPickerFragment.Companion.getEXTRA_WEIGHT(), 0));
            mWeightButton.setText(mWeight.getWeightStringKg());
        }

        updateUi();
    }

    private void updateUi() {
        mDateButton.setText(Time.getDateString(mContext, "EEE, ", mWeight.getTime()));
        mTimeButton.setText(Time.getTimeString(mContext, mWeight.getTime()));
        mWeightButton.setText(mWeight.getWeightStringKg());
        Bmi.updateAssessmentView(mContext,
                mView,
                R.id.assessment_layout,
                R.id.bmi_tv,
                mWeight.bmi(),
                true);
    }
}
