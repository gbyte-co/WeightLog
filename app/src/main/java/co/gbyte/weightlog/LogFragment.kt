package co.gbyte.weightlog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import co.gbyte.weightlog.model.Weight
import co.gbyte.weightlog.model.WeightLab

import java.util.*

import kotlinx.android.synthetic.main.fragment_weight_log.*

class LogFragment : Fragment() {
    private var mMenu: Menu? = null

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weight_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weightLab = WeightLab.get(context)
        val weights = weightLab.weights as ArrayList<Weight>

        linearLayoutManager = LinearLayoutManager(context)
        weight_recycler_view.layoutManager = linearLayoutManager

        adapter = RecyclerAdapter(weights)
        weight_recycler_view.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_log_menu, menu)
        mMenu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_settings -> {
                showSettings()
                return true
            }
            /*
            R.id.menu_item_edit_weight -> {
                if (mWeight != null) {
                    val intent = WeightActivity.newIntent(context, mWeight?.id)
                    startActivity(intent)
                }
                return true
            }
            R.id.menu_item_test -> {
                val testDialog = TestDialog()
                testDialog.show(activity?.supportFragmentManager, "Test Dialog")
                return true
            }
            */
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showSettings() {
        val intent = Intent(context, SettingsActivity::class.java)
        startActivity(intent)
    }
}
