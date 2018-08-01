package co.gbyte.weightlog

import android.content.Context
import android.content.SharedPreferences

object Settings {

    private const val NAME = "WeightLog"
    private const val MODE = Context.MODE_PRIVATE
    lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(
            operation: (SharedPreferences.Editor) -> Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private const val SHOW_BMI_KEY = "show_bmi_key"
    private val SHOW_BMI = Pair(SHOW_BMI_KEY, false)
    var showBmi: Boolean
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getBoolean(SHOW_BMI.first, SHOW_BMI.second)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putBoolean(SHOW_BMI.first, value)
        }

    private const val HEIGHT_KEY = "height"
    // ToDo: hardcoded?!
    //private const val INITIAL_HEIGHT = 160
    private const val INITIAL_HEIGHT = 173
    private val HEIGHT = Pair(HEIGHT_KEY, INITIAL_HEIGHT)
    var height: Int
        get() = preferences.getInt(HEIGHT.first, HEIGHT.second)
        set(value) = preferences.edit {
            it.putInt(SHOW_BMI.first, value)
        }
}
