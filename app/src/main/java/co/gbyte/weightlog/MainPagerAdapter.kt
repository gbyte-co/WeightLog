package co.gbyte.weightlog;

/**
 * Created by walt on 29/12/16.
 *
 */

class MainPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    MainPagerAdapter(android.support.v4.app.FragmentManager mgr) {
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

    @Override
    public int getCount() {
        return 2;
    }
}
