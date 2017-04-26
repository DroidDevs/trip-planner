package droiddevs.com.tripplanner.tripdetails;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.tripdestination.TripDestinationFragment;
import droiddevs.com.tripplanner.tripmap.TripMapFragment;

/**
 * Created by elmira on 4/13/17.
 */

public class TripDetailsFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final String LOG_TAG = "TripDetailsPagerAdapter";
    private WeakReference<Context> context;

    //private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private Trip mTrip;

    public TripDetailsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = new WeakReference<>(context);
    }

    public void setTrip(Trip trip) {
        this.mTrip = trip;
        notifyDataSetChanged();

        /*if (mTrip.getDestinations() != null) {
            for (int i = 0; i < mTrip.getDestinations().size(); i++) {
                TripDestinationFragment fragment = (TripDestinationFragment) getRegisteredFragment(i + 1);
                if (fragment != null) {
                    fragment.setDestinationId(mTrip.getDestinations().get(i).getDestinationId());
                }
            }
        }*/
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        //registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    // Returns the fragment for the position (if instantiated)
    /*public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }*/

    @Override
    public Fragment getItem(int position) {
        Log.d(LOG_TAG, "getItem() position: " + position);
        if (position == 0) {
            return TripMapFragment.newInstance(mTrip.getTripId());
        }
        else {
            Log.d(LOG_TAG, "destinationId: "+mTrip.getDestinations().get(position - 1).getDestinationId());
            return TripDestinationFragment.newInstance(mTrip.getDestinations().get(position - 1).getDestinationId(), mTrip.getTripId());
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d(LOG_TAG, "getPageTitle() position: " + position);
        if (position == 0) {
            SpannableString sb = new SpannableString("  ");
            Drawable icon = ContextCompat.getDrawable(context.get(), R.drawable.ic_map_white);
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(icon, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
        Destination destination = mTrip.getDestinations().get(position - 1);
        /*TripDestinationFragment fragment = (TripDestinationFragment) getRegisteredFragment(position);
        Log.d(LOG_TAG, "fragment: " + fragment);
        if (fragment != null) {
            fragment.setDestinationId(destination.getDestinationId());
        }*/
        return destination.getName();
    }

    @Override
    public int getCount() {
        if (mTrip == null) return 0;
        return 1 + (mTrip.getDestinations() == null ? 0 : mTrip.getDestinations().size());
    }
}