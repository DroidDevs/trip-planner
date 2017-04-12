package droiddevs.com.tripplanner.model.map;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;

import droiddevs.com.tripplanner.model.Destination;

/**
 * Created by elmira on 4/11/17.
 */

public class TripDestinationMapItem extends BaseMapItem {

    private static SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");

    private String startDate;
    private String endDate;
    private String duration;

    public TripDestinationMapItem(Destination destination, int position) {
        super(new LatLng(destination.getLatitude(), destination.getLongitude()), destination.getName(), position);

        startDate = sdf.format(destination.getStartDate());
        endDate = sdf.format(destination.getEndDate());
        duration = destination.getDuration() +" days";
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDuration() {
        return duration;
    }
}
