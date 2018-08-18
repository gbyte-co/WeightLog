package co.gbyte.weightlog

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference

class HeightPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {

    private val mDialogLayoutResId = R.layout.dialog_height
    //private var mPrefs: SharedPreferences? = null

    init {
        positiveButtonText = context.getText(R.string.height_pref_set)
        negativeButtonText = context.getText(R.string.height_pref_cancel)
    }

    override fun getDialogLayoutResource(): Int {
        return mDialogLayoutResId
    }
}
