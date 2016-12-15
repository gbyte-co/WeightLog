package co.gbyte.weightlog;

import android.content.SharedPreferences;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static android.R.attr.key;

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

