package co.gbyte.weightlog

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*

import co.gbyte.weightlog.R.string.bmi_pref_key
import co.gbyte.weightlog.R.string.height_pref_key

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        addFragment(SettingsFragment(), R.id.fragment_container)
    }

    class SettingsFragment: PreferenceFragmentCompat() {

        private var mAssessmentPrefCategory: PreferenceCategory? = null
        private var mPrefs: SharedPreferences = Settings.preferences
        private var mListener: SharedPreferences.OnSharedPreferenceChangeListener? = null
        private var mBmiPref: SwitchPreference? = null
        private var mHeightPref: HeightPreference? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            mBmiPref = findPreference(getString(bmi_pref_key)) as SwitchPreference?
            //mHeightPref = findPreference(getString(height_pref_key))
            mListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
                mPrefs = prefs
                val pref = preferenceScreen.findPreference(key)
                mAssessmentPrefCategory = findPreference(resources.getString(R.string.assessment_pref_category_key)) as PreferenceCategory

                // Use instance field for mListener
                // It will not be gc'd as long as this instance is kept referenced
                mPrefs.registerOnSharedPreferenceChangeListener(mListener)
                updatePreference(pref)
            }
        }

        override fun onPause() {
            super.onPause()
            mPrefs.unregisterOnSharedPreferenceChangeListener(mListener)
        }

        override fun onResume() {
            super.onResume()
            updatePreferences()
        }

        private fun updatePreferences() {
            for (i in 0 until preferenceScreen.preferenceCount) {
                val preference = preferenceScreen.getPreference(i)
                if (preference is PreferenceGroup) {
                    for (j in 0 until preference.preferenceCount) {
                        val singlePref = preference.getPreference(j)
                        updatePreference(singlePref)
                    }
                } else {
                    updatePreference(preference)
                }
            }
            mPrefs.registerOnSharedPreferenceChangeListener(mListener)
        }

        private fun updatePreference(preference: Preference?) {
            if (preference?.key == resources.getString(bmi_pref_key)) {
                val isOn = Settings.showBmi
                if (isOn) {
                    //mAssessmentPrefCategory?.addPreference(mHeightPref)
                    //updatePreference(mHeightPref)
                } else {
                    //mAssessmentPrefCategory?.removePreference(mHeightPref)
                    mBmiPref?.isChecked = isOn
                }
            }
            if (preference?.key == getString(height_pref_key)) {
                var height: Int = mPrefs.getInt(preference.key, 0)
                // if metric:
                height /= 10
                preference.summary = "Set to " + height.toString() + " cm"
                // ToDo: if imperial
            }
        }
    }
}

