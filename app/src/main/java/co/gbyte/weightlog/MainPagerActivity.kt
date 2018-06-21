package co.gbyte.weightlog

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainPagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_pager)

        val pager = findViewById<ViewPager>(R.id.main_pager)
        pager!!.adapter = MainPagerAdapter(supportFragmentManager)

        val fab = findViewById<FloatingActionButton>(R.id.fab_new_weight)

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
