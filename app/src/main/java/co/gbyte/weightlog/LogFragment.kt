package co.gbyte.weightlog

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.gbyte.weightlog.R.id.weight_recycler_view

import java.util.*

import co.gbyte.weightlog.model.Weight
import co.gbyte.weightlog.model.WeightLab

import kotlinx.android.synthetic.main.fragment_weight_log.*

class LogFragment : Fragment() {
    //private var mWeightRecyclerView: RecyclerView? = null
    //private var mWeight: Weight? = null
    //private var mMenu: Menu? = null

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

        //mWeightRecyclerView = weight_recycler_view
        //mWeightRecyclerView?.layoutManager =  LinearLayoutManager(context)

        /*
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        if (!settings.contains(getString(R.string.bmi_pref_key))) {
            // The app is running for the first time. Ask user for basic settings.
            showSettings()
        }
        updateUI()
        */
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_log_menu, menu)
        //mMenu = menu
        updateMenu()
    }

    /*
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_item_settings -> {
                showSettings()
                return true
            }
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
            else -> return super.onOptionsItemSelected(item)
        }
    }
    */

    private fun showSettings() {
        val intent = Intent(context, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun updateUI() {

        adapter.notifyDataSetChanged()
        // ToDo: find out if we still should:
        // switch to:
        // adapter.notifyItemChanged(<position>);
        // The challenge may be discovering which position has
        // changed and reloading the correct item.
    }

    private fun updateMenu() {
        //val editMenuItem = mMenu?.findItem(R.id.menu_item_edit_weight)
        //editMenuItem?.isVisible = mWeight != null
    }
}
