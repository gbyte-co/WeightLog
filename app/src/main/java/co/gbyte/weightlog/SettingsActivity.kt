package co.gbyte.weightlog

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceGroup
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

import co.gbyte.weightlog.R.string.bmi_pref_key
import co.gbyte.weightlog.R.string.height_pref_key

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        addFragment(SettingsFragment(), R.id.fragment_container)
    }

    override fun onBackPressed() {
        finish()
        // Behave like navigate up.
        // In particular apply all the changes in settings
        super.onSupportNavigateUp()
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
            mHeightPref = findPreference(getString(height_pref_key)) as HeightPreference?
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

        override fun onDisplayPreferenceDialog(preference: Preference) {
            // Try if the preference is one of our custom Preferences
            var dialogFragment: DialogFragment? = null
            if (preference is HeightPreference) {
                // Create a new instance of TimePreferenceDialogFragment with the key of the related
                // Preference
                dialogFragment = HeightPreferenceDialogFragment.newInstance(preference.key)
            }

            // If it was one of our custom Preferences, show its dialog
            if (dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0)
                dialogFragment.show(this.fragmentManager,
                        "android.support.v7.preference" + ".PreferenceFragment.DIALOG")
            } else {
                super.onDisplayPreferenceDialog(preference)
            }// Could not be handled here. Try with the super method.
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
                if (mPrefs is PreferenceGroup) {
                    val preferenceGroup = preference as PreferenceGroup
                    for (j in 0 until preferenceGroup.preferenceCount) {
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
                    mAssessmentPrefCategory?.addPreference(mHeightPref as Preference?)
                    updatePreference(mHeightPref as Preference?)
                } else {
                    mAssessmentPrefCategory?.removePreference(mHeightPref as Preference?)
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

