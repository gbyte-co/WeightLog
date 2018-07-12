package co.gbyte.weightlog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class AcitivityUtils {

    companion object {

        fun addFragmentToActivity(manager: FragmentManager, fragment: Fragment, frameId: Int) {

            val transaction = manager.beginTransaction()
            transaction.add(frameId, fragment)
            transaction.commit()
        }
    }
}