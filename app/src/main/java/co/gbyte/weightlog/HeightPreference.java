package co.gbyte.weightlog;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by walt on 2/12/16.
 */

public class HeightPreference extends DialogPreference {

    private NumberPicker numberPicker = null;
    private int lastValue = 0;

    public HeightPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(context.getText(R.string.number_preference_set));
        setNegativeButtonText(context.getText(R.string.number_preference_cancel));
    }

    @Override
    protected View onCreateDialogView() {
        numberPicker = new NumberPicker(getContext());
        return (numberPicker);
    }

    @Override
    protected  void onBindDialogView(View v) {
        super.onBindDialogView(v);
        numberPicker.setValue(lastValue);
    }
}

