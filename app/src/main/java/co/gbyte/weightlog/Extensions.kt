package co.gbyte.weightlog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import android.text.format.DateFormat
import java.text.SimpleDateFormat

import java.util.*


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false) : View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

/**
 * Former Utils.Time
 */
fun Date.getTimeString(context: Context) : String
        = DateFormat.getTimeFormat(context).format(this)

fun Date.getShortDateString(context: Context) :String
        = DateFormat.getDateFormat(context).format(this)

fun Date.getDateString(context: Context, format: String) : String {
    return (SimpleDateFormat(format, Locale.getDefault()).format(this)
            + DateFormat.getMediumDateFormat(context).format(this))
}

