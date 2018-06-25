package co.gbyte.weightlog

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton

import kotlinx.android.synthetic.main.main_activity_pager.main_pager
import kotlinx.android.synthetic.main.main_activity_pager.view.*

class MainPagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity_pager)

        val pager: ViewPager = main_pager
        pager.adapter  = MainPagerAdapter(supportFragmentManager)

        val fab: FloatingActionButton? = pager.fab_weight

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (pager.currentItem == 0) {
                    fab?.visibility = View.VISIBLE
                } else {
                    pager.visibility = View.GONE
                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}
