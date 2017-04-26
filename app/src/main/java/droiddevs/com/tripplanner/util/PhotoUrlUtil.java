package droiddevs.com.tripplanner.util;

import android.util.Log;

import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;

/**
 * Created by elmira on 4/20/17.
 */

public class PhotoUrlUtil {

    public static String getPhotoUrl(String photoReference) {
        if (photoReference == null || "".equals(photoReference.trim())) return null;
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&photoreference="
                + photoReference.trim() + "&key=" + TripPlannerApplication.getGooglePlacesApiKey();
    }

    public static String getTripMapPhotoUrl(Trip trip) {
        if (trip.getDestinations() == null || trip.getDestinations().size() == 0) return null;

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap?size=640x640");
        sb.append("&key=").append(TripPlannerApplication.getGooglePlacesApiKey());
        for (Destination destination : trip.getDestinations()) {
            try {
                destination.fetchIfNeeded();

                sb.append("&markers=color:0xD81B60|");
                sb.append(destination.getLatitude()).append(",");
                sb.append(destination.getLongitude());
            }
            catch (Throwable ex){
                Log.e("PhotoUrlUtil", ex.toString(), ex);
                return null;
            }
        }
        return sb.toString();
    }
}
