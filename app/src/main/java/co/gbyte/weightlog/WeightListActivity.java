package co.gbyte.weightlog;

import android.support.v4.app.Fragment;

/**
 * Created by walt on 19/10/16.
 *
 */

public class WeightListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new WeightListFragment();
    }
}
