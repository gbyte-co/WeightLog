package co.gbyte.weightlog

import android.content.Context
import android.content.Intent

import java.util.UUID

import androidx.fragment.app.Fragment

class WeightActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        val weightId: UUID?  = intent.getSerializableExtra(EXTRA_WEIGHT_ID) as UUID
        return if (weightId == null)
                WeightFragment.newInstance() else WeightFragment.newInstance(weightId)
    }

    companion object {
        private val EXTRA_WEIGHT_ID = "co.gbyte.weightlog.weight_id"

        /*
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, WeightActivity::class.java)
        }
        */

        fun newIntent(packageContext: Context, weightId: UUID): Intent {
            val intent = Intent(packageContext, WeightActivity::class.java)
            intent.putExtra(EXTRA_WEIGHT_ID, weightId)
            return intent
        }
    }
}
