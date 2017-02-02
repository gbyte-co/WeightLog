package co.gbyte.weightlog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import co.gbyte.android.weightpicker.WeightPicker;

/**
 * Created by walt on 21/10/16.
 *
 */

public class WeightPickerFragment extends DialogFragment {

    public static final String EXTRA_WEIGHT = "co.gbyte.android.weightlog.weight";
    private static final String ARG_WEIGHT = "weight";

    private int mWeight;
    private WeightPicker mWeightPicker;

    public static WeightPickerFragment newInstance(int weight) {
        Bundle args = new Bundle();
        args.putInt(ARG_WEIGHT, weight);

        WeightPickerFragment fragment = new WeightPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mWeight = getArguments().getInt(ARG_WEIGHT);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_weight, null);

        mWeightPicker = (WeightPicker) v.findViewById(R.id.dialog_weight_weight_picker);
        mWeightPicker.setValue(mWeight);
        mWeightPicker.setOnValueChangedListener(new WeightPicker.OnValueChangedListener() {
            @Override
            public void onValueChange(WeightPicker picker, int oldValue, int newValue) {
                mWeight = mWeightPicker.getValue();
                getArguments().putInt(EXTRA_WEIGHT, mWeight);
            }
        });

        mWeightPicker.clearFocus();
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.weight_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendResult(Activity.RESULT_OK, mWeight);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, int weight) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_WEIGHT, weight);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
