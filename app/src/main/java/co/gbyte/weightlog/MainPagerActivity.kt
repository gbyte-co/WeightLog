package co.gbyte.weightlog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.main_activity_pager.*

class MainPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_pager)

        /*
        if (!Settings.firstRun) {
            Settings.firstRun = true
        }
        */

        val pager: ViewPager? = main_pager
        pager?.adapter  = MainPagerAdapter(supportFragmentManager)

        val fab: FloatingActionButton? = fab_weight
        fab?.setOnClickListener {
            val intent = Intent(this, WeightActivity::class.java)
            startActivity(intent)
        }

        pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                fab?.visibility = View.GONE
                if (pager.currentItem == 0) {
                    fab?.visibility = View.VISIBLE
                } else {
                    fab?.visibility = View.GONE
                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}
