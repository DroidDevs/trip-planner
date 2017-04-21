package droiddevs.com.tripplanner.util;

import droiddevs.com.tripplanner.application.TripPlannerApplication;

/**
 * Created by elmira on 4/20/17.
 */

public class PhotoUrlUtil {

    public static String getPhotoUrl(String photoReference) {
        if (photoReference == null || "".equals(photoReference.trim())) return null;
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&photoreference="
                + photoReference.trim() + "&key=" + TripPlannerApplication.getGooglePlacesApiKey();
    }
}
