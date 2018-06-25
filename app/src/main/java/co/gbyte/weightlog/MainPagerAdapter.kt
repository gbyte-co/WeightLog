package co.gbyte.weightlog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

internal class MainPagerAdapter(mgr: FragmentManager) : FragmentPagerAdapter(mgr) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return ChartFragment()
            1 -> return ChartFragment()
        }
        return null
    }

    override fun getCount(): Int {
        return 2
    }
}
