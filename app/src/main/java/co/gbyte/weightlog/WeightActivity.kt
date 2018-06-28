package co.gbyte.weightlog;

import android.content.Context;
import android.content.Intent;

import java.util.UUID;

import androidx.fragment.app.Fragment;

public class WeightActivity extends SingleFragmentActivity {

    private static final String EXTRA_WEIGHT_ID = "co.gbyte.weightlog.weight_id";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, WeightActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, UUID weightId) {
        Intent intent = new Intent(packageContext, WeightActivity.class);
        intent.putExtra(EXTRA_WEIGHT_ID, weightId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID weightId;
        if((weightId = (UUID) getIntent().getSerializableExtra(EXTRA_WEIGHT_ID)) == null) {
            return WeightFragment.newInstance();
        }
        return WeightFragment.newInstance(weightId);
    }
}
