package co.gbyte.weightlog;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
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

    private Weight mWeight;
    private boolean mIsNew = false;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mWeightButton;
    private EditText mNoteField;

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
            // New entry:
            mWeight = new Weight();
            mWeight.setWeight(WeightLab.get(getActivity()).getLastWeight());
        } else {
            UUID weightId = (UUID) getArguments().getSerializable(ARG_WEIGHT_ID);
            mWeight = WeightLab.get(getActivity()).getWeight(weightId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weight, container, false);
        mDateButton = (Button) v.findViewById(R.id.weight_date_button);
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

        mTimeButton = (Button) v.findViewById(R.id.weight_time_button);
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

        mWeightButton = (Button) v.findViewById(R.id.weight_button);
        mWeightButton.setText(mWeight.getWeightString());
        mWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                WeightPickerFragment dialog = WeightPickerFragment.newInstance(mWeight.getWeight());
                dialog.setTargetFragment(WeightFragment.this, REQUEST_WEIGHT);
                dialog.show(manager, DIALOG_WEIGHT);
            }
        });

        mNoteField = (EditText) v.findViewById(R.id.weight_note);
        mNoteField.setText(mWeight.getNote());
        mNoteField.addTextChangedListener(new TextWatcher() {
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

        return v;
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
        inflater.inflate(R.menu.fragment_weight, menu);
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

            default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void goBackToList() {
        Intent intent = new Intent(getActivity(), WeightListActivity.class);
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
            mWeightButton.setText(mWeight.getWeightString());
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
