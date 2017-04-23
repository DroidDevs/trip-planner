package droiddevs.com.tripplanner.model.map;

import android.content.Context;
import android.text.format.DateUtils;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Destination;

/**
 * Created by elmira on 4/11/17.
 */

public class TripDestinationMapItem extends BaseMapItem {

    private static SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");

    private String dateRange;
    private String duration;

    public TripDestinationMapItem(Context context, Destination destination, int position) {
        super(new LatLng(destination.getLatitude(), destination.getLongitude()), destination.getName(), position, destination.getPhotoUrl());

        setIconResId(R.drawable.ic_map_place_color_primary_big);
        setSelectedIconResId(R.drawable.ic_map_place_color_accent_big);

        dateRange = DateUtils.formatDateRange(context, destination.getStartDate().getTime(), destination.getEndDate().getTime(), DateUtils.FORMAT_SHOW_DATE);
        duration = destination.getDuration() + " days";
    }

    public String getDateRange() {
        return dateRange;
    }

    public String getDuration() {
        return duration;
    }
}
