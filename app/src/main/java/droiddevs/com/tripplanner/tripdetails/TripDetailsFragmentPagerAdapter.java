package droiddevs.com.tripplanner.tripdetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.tripmap.TripMapFragment;

/**
 * Created by elmira on 4/13/17.
 */

public class TripDetailsFragmentPagerAdapter extends FragmentPagerAdapter {

    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private Trip mTrip;

    public TripDetailsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setTrip(Trip trip) {
        this.mTrip = trip;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    // Returns the fragment for the position (if instantiated)
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return TripMapFragment.newInstance(mTrip.getTripId());
        }
        else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Map";
        }
        return mTrip.getDestinations().get(position - 1).getName();
    }

    @Override
    public int getCount() {
        if (mTrip == null) return 0;
        return 1 + (mTrip.getDestinations() == null ? 0 : mTrip.getDestinations().size());
    }
}