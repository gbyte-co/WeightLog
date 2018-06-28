package co.gbyte.weightlog;

import android.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import co.gbyte.android.weightpicker.WeightPicker;

/**
 * Created by walt on 28/01/17.
 *
 */

public class TestDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_test, null);

        final TextView testedValue = (TextView) v.findViewById(R.id.tested_value);

        final WeightPicker picker = (WeightPicker) v.findViewById(R.id.tested_picker);

        picker.setOnValueChangedListener(new WeightPicker.OnValueChangedListener() {
            @Override
            public void onValueChange(WeightPicker picker, int oldVal, int newVal ) {
                testedValue.setText(String.format(Locale.getDefault(), "Current value: %d", newVal));
            }
        });


        testedValue.setText(String.format(Locale.getDefault(), "Current value: %d",
                                          picker.getValue()));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Test Dialog")
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
