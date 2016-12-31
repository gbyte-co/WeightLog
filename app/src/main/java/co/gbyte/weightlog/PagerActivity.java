package co.gbyte.weightlog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by walt on 29/12/16.
 *
 */

public class PagerActivity extends AppCompatActivity {




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        ViewPager pager = (ViewPager) findViewById(R.id.main_pager);
        pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
    }
}
