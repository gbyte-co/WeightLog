package co.gbyte.weightlog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.SwitchPreference;

import androidx.appcompat.app.AppCompatActivity;

import static co.gbyte.weightlog.R.string.bmi_pref_key;
import static co.gbyte.weightlog.R.string.height_pref_key;

/**
 * Created by walt on 22/11/16.
 *
 */

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {

        private SharedPreferences mPrefs;
        private SharedPreferences.OnSharedPreferenceChangeListener mListener;
        private PreferenceCategory mAssessmentPrefCategory;
        private HeightPreference mHeightPref;

        private SwitchPreference mBmiPref = null;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            mPrefs = getPreferenceManager().getSharedPreferences();
            mAssessmentPrefCategory = (PreferenceCategory)
                    findPreference(getResources().getString(R.string.assessment_pref_category_key));
            mBmiPref = (SwitchPreference)  findPreference(getString(R.string.bmi_pref_key));
            mHeightPref = (HeightPreference)
                    findPreference(getResources().getString(height_pref_key));

            // Use instance field for mListener
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

            if(preference.getKey().equals(getResources().getString(bmi_pref_key))) {
                boolean isOn = mPrefs.getBoolean(preference.getKey(), false);
                if(isOn) {
                    mAssessmentPrefCategory.addPreference(mHeightPref);
                    updatePreference(mHeightPref);
                } else {
                    mAssessmentPrefCategory.removePreference(mHeightPref);
                    mBmiPref.setChecked(isOn);
                }
            }

            if(preference.getKey().equals(getString(height_pref_key))) {
                Integer height = mPrefs.getInt(preference.getKey(), 0);
                // if metric:
                height /= 10;
                preference.setSummary("Set to " + (height).toString() + " cm");
                // ToDo: if imperial
            }
        }
    }
}

