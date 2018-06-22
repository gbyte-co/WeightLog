package co.gbyte.weightlog

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.main_activity_pager.*
import kotlinx.android.synthetic.main.main_activity_pager.view.*

class MainPagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_pager)

        val pager = main_pager

        pager.adapter = MainPagerAdapter(supportFragmentManager)

        val fab = pager.fab_new_weight

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (pager.currentItem == 0) {
                    fab!!.visibility = View.VISIBLE
                } else {
                    pager.visibility = View.GONE
                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}
