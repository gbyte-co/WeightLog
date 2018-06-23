package co.gbyte.weightlog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import co.gbyte.weightlog.model.Weight;
import co.gbyte.weightlog.model.WeightLab;

/**
 * Created by walt on 23/10/16.
 *
 */

public class ConfirmDialogFragment extends DialogFragment {

    private static final String ARG_WEIGHT_ID = "weightId";

    public static ConfirmDialogFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEIGHT_ID, id);

        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final UUID id = (UUID) getArguments().getSerializable(ARG_WEIGHT_ID);

        Weight weight = WeightLab.get(getActivity()).getWeight(id);
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_confirm_delete, null);

        TextView weightTextView =
                (TextView) v.findViewById(R.id.dialog_confirm_delete_weight_weight);
        weightTextView.setText(weight.getWeightStringKg());

        TextView dateTextView = (TextView) v.findViewById(R.id.dialog_confirm_delete_weight_date);
        dateTextView.setText(DateFormat.getDateFormat(getActivity()).format(weight.getTime()));

        TextView timeTextView = (TextView) v.findViewById(R.id.dialog_confirm_delete_weight_time);
        timeTextView.setText(DateFormat.getTimeFormat(getActivity()).format(weight.getTime()));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.confirm_delete_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                WeightLab.get(getActivity()).deleteWeight(id);
                                Intent intent = new Intent(getActivity(), MainPagerActivity.class);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
