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

/**
 * Created by walt on 21/10/16.
 *
 */

public class WeightPickerFragment extends DialogFragment {

    public static final String EXTRA_WEIGHT = "co.gbyte.android.weightlog.weight";
    private static final String ARG_WEIGHT = "weight";
    private static final int KILO = 1000;
    private static final int DECA = 100;

    private int mWeight;
    private DecimalNumberPicker mWeightPicker;

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

        mWeightPicker = (DecimalNumberPicker) v.findViewById(R.id.dialog_weight_weight_picker);
        mWeightPicker.init((float) mWeight / KILO);
        mWeightPicker.setOnValueChangedListener(new DecimalNumberPicker.OnValueChangedListener() {
            @Override
            public void onValueChanged(DecimalNumberPicker view, int integerPart, int decimalPart) {
                mWeight = integerPart * KILO + decimalPart * DECA;
                getArguments().putInt(EXTRA_WEIGHT, mWeight);
                //weightDialog.setTitle("New weight: " +formatted(mWeight));
            }
        });

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

    /*
    private String formatted(int mWeight) {
        return new DecimalFormat("##0.0 kg").format((float) mWeight / KILO);
    }
    */
}
