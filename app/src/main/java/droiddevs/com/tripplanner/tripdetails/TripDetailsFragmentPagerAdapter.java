package droiddevs.com.tripplanner.tripdetails;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import java.lang.ref.WeakReference;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.tripdestination.TripDestinationFragment;
import droiddevs.com.tripplanner.tripmap.TripMapFragment;

/**
 * Created by Elmira Andreeva on 4/13/17.
 */

public class TripDetailsFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final String LOG_TAG = "TripDetailsPagerAdapter";
    private WeakReference<Context> context;

    private Trip mTrip;

    public TripDetailsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = new WeakReference<>(context);
    }

    public void setTrip(Trip trip) {
        this.mTrip = trip;

        Log.d(LOG_TAG, "setTrip()");

        if (mTrip.getDestinations() != null) {
            for (int i = 0; i < mTrip.getDestinations().size(); i++) {
                TripDestinationFragment fragment = (TripDestinationFragment) getItem(i + 1);
                Log.d(LOG_TAG, "position "+i+", fragment: "+fragment);
                if (fragment != null) {
                    fragment.setDestinationId(mTrip.getDestinations().get(i).getDestinationId());
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public Parcelable saveState()
    {
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(LOG_TAG, "getItem() position: " + position);
        if (position == 0) {
            return TripMapFragment.newInstance(mTrip.getTripId());
        }
        else {
            Log.d(LOG_TAG, "destinationId: " + mTrip.getDestinations().get(position - 1).getDestinationId());
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
        return destination.getName();
    }

    @Override
    public int getCount() {
        if (mTrip == null) return 0;
        return 1 + (mTrip.getDestinations() == null ? 0 : mTrip.getDestinations().size());
    }
}