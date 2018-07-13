package co.gbyte.weightlog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class ActivityUtils {

    companion object {

        fun addFragmentToActivity(manager: FragmentManager, fragment: Fragment, frameId: Int) {

            val transaction = manager.beginTransaction()
            transaction.add(frameId, fragment)
            transaction.commit()
        }
    }
}