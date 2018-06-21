package co.gbyte.weightlog

/**
 * Created by walt on 29/12/16.
 *
 */

internal class MainPagerAdapter(mgr: android.support.v4.app.FragmentManager) :
        android.support.v4.app.FragmentPagerAdapter(mgr) {

    override fun getItem(position: Int): android.support.v4.app.Fragment? {
        when (position) {
            0 -> return LogFragment()
            1 -> return ChartFragment()
        }
        return null
    }

    override fun getCount(): Int {
        return 2
    }
}
