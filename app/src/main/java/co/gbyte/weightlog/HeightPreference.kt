package co.gbyte.weightlog

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference
import android.content.res.TypedArray



class HeightPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {

    var height = 0
        set(value) {
            persistInt(value)
            field = value
        }

    private val mDialogLayoutResId = R.layout.dialog_height

    init {
        positiveButtonText = context.getText(R.string.height_pref_set)
        negativeButtonText = context.getText(R.string.height_pref_cancel)
    }

    /**
     * Called when a Preference is being inflated and the default value attribute needs to be read
     */
    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        // The type of this preference is Int, so we read the default value from the attributes
        // as Int. Fallback value is set to 0.
        return a!!.getInt(index, 0)
    }

    override fun getDialogLayoutResource(): Int {
        return mDialogLayoutResId
    }
}
