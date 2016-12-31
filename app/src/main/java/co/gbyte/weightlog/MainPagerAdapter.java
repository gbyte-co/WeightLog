package co.gbyte.weightlog;

/**
 * Created by walt on 29/12/16.
 *
 */

public class MainPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private static final String KEY_POSITION = "key_position";

    public MainPagerAdapter(android.support.v4.app.FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LogFragment();
            case 1:
                return new ChartFragment();
        }
        return null;
    }

    /*
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                LogFragment logFragment = new LogFragment();
                Bundle args = new Bundle();
                args.putInt("KEY_POSITION", position);
                logFragment.setArguments(args);
                return(logFragment);
                android.support.v4.app.Fragment f = new LogFragment();
                return f;
                return new Fragment();

            case 0:
                return new Fragment();
        }
        return null;
    }
    */



    @Override
    public int getCount() {
        return 2;
    }
}
