package co.gbyte.weightlog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

/**
 * Created by walt on 22/11/16.
 *
 */

public class SettingsFragment extends PreferenceFragment {

    SharedPreferences mPrefs;
    SharedPreferences.OnSharedPreferenceChangeListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mPrefs = getPreferenceManager().getSharedPreferences();

        // Use instance field for listener
        // It will not be gc'd as long as this instance is kept referenced
        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                mPrefs = prefs;
                Preference pref = getPreferenceScreen().findPreference(key);
                updatePreference(pref);
            }
        };

        mPrefs.registerOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        mPrefs.unregisterOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    Preference singlePref = preferenceGroup.getPreference(j);
                    updatePreference(singlePref);
                }
            } else {
                updatePreference(preference);
            }
        }
        mPrefs.registerOnSharedPreferenceChangeListener(mListener);
    }

    private void updatePreference(Preference preference) {
        if (preference == null) {
            return;
        }

        switch (preference.getKey()) {
            case ("preference_height") :
                String s = mPrefs.getString(preference.getKey(), "");
                preference.setSummary("Set to " + s + " cm");
                break;

            default :
        }
    }
}
