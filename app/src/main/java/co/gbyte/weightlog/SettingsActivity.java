package co.gbyte.weightlog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by walt on 22/11/16.
 *
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentById(android.R.id.content)==null) {
            getFragmentManager().beginTransaction().add(android.R.id.content,
                    new SettingsFragment()).commit();
        }
    }
}

