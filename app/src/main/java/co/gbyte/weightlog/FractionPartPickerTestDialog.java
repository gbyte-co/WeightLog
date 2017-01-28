package co.gbyte.weightlog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

import co.gbyte.android.weightpicker.FractionPartPicker;

/**
 * Created by walt on 28/01/17.
 *
 */

public class FractionPartPickerTestDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fraction_part_picker_test_dialog, null);

        final TextView testedValue = (TextView) v.findViewById(R.id.tested_value);

        final FractionPartPicker picker = (FractionPartPicker) v.findViewById(R.id.tested_picker);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                testedValue.setText(String.format(Locale.getDefault(), "Current value: %d",
                        picker.getActualValue()));
            }
        });

        testedValue.setText(String.format(Locale.getDefault(),
                                          "Current value: %d",
                                          picker.getActualValue()));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Test Dialog")
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
