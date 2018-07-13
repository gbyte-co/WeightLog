package co.gbyte.weightlog

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*

//import co.gbyte.weightlog.R.string.bmi_pref_key
//import co.gbyte.weightlog.R.string.height_pref_key

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        addFragment(SettingsFragment(), R.id.fragment_container)
    }

    class SettingsFragment: PreferenceFragmentCompat() {

        /*
        private var mPrefs: SharedPreferences? = null
        private var mListener: SharedPreferences.OnSharedPreferenceChangeListener? = null
        private var mAssessmentPrefCategory: PreferenceCategory? = null
        private var mHeightPref: HeightPreference? = null

        private var mBmiPref: SwitchPreference? = null
        */
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        }
        /*
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)

            mPrefs = preferenceManager.sharedPreferences
            mAssessmentPrefCategory = findPreference(resources.getString(R.string.assessment_pref_category_key)) as PreferenceCategory
            mBmiPref = findPreference(getString(R.string.bmi_pref_key)) as SwitchPreference
            mHeightPref = findPreference(resources.getString(height_pref_key)) as HeightPreference

            // Use instance field for mListener
            // It will not be gc'd as long as this instance is kept referenced
            mListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
                mPrefs = prefs
                val pref = preferenceScreen.findPreference(key)
                //updatePreference(pref)
            }

            mPrefs!!.registerOnSharedPreferenceChangeListener(mListener)
        }


        override fun onPause() {
            super.onPause()
            mPrefs!!.unregisterOnSharedPreferenceChangeListener(mListener)
        }

        override fun onResume() {
            super.onResume()

            for (i in 0 until preferenceScreen.preferenceCount) {
                val preference = preferenceScreen.getPreference(i)
                if (preference is PreferenceGroup) {
                    for (j in 0 until preference.preferenceCount) {
                        val singlePref = preference.getPreference(j)
                        //updatePreference(singlePref)
                    }
                } else {
                    //updatePreference(preference)
                }
            }
            mPrefs!!.registerOnSharedPreferenceChangeListener(mListener)
        }


        private fun updatePreference(preference: Preference?) {
            if (preference == null) {
                return
            }

            if (preference.key == resources.getString(bmi_pref_key)) {
                val isOn = mPrefs!!.getBoolean(preference.key, false)
                if (isOn) {
                    mAssessmentPrefCategory!!.addPreference(mHeightPref)
                    updatePreference(mHeightPref)
                } else {
                    mAssessmentPrefCategory!!.removePreference(mHeightPref)
                    mBmiPref!!.isChecked = isOn
                }
            }

            if (preference.key == getString(height_pref_key)) {
                var height: Int? = mPrefs!!.getInt(preference.key, 0)
                // if metric:
                height /= 10
                preference.summary = "Set to " + height.toString() + " cm"
                // ToDo: if imperial
            }
        }
        */
    }
}

