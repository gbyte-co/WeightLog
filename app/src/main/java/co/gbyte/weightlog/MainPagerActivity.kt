package co.gbyte.weightlog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.main_activity_pager.*

class MainPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_pager)

        Settings.init(application)

        val pager: ViewPager = main_pager
        pager.adapter  = MainPagerAdapter(supportFragmentManager)

        fab_weight.setOnClickListener {
            val intent = Intent(this, WeightActivity::class.java)
            startActivity(intent)
        }

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                fab_weight.visibility = View.GONE
                if (pager.currentItem == 0) {
                    fab_weight.visibility = View.VISIBLE
                } else {
                    fab_weight.visibility = View.GONE
                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}
