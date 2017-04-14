package droiddevs.com.tripplanner.model.util;

import droiddevs.com.tripplanner.model.Point;
import droiddevs.com.tripplanner.model.source.remote.PlaceDetailsResponse;

/**
 * Created by elmira on 4/13/17.
 */

public class PlacePointConverter {

    public static Point convertToPoint(PlaceDetailsResponse response) {

        PlaceDetailsResponse.PlaceDetails placeDetails = response.getPlaceDetails();
        Point point = new Point();

        point.setPointId(placeDetails.getPlaceId());
        point.setName(placeDetails.getName());

        point.setRating(placeDetails.getRating());
        point.setPhoneNumber(placeDetails.getPhoneNumber() == null ? "" : placeDetails.getPhoneNumber());

        point.setLatitude(placeDetails.getGeometry().getLocation().getLatitude());
        point.setLongitude(placeDetails.getGeometry().getLocation().getLongitude());

        if (placeDetails.getPhotos() != null && placeDetails.getPhotos().size() > 0) {
            point.setPhotoReference(placeDetails.getPhotos().get(0).getPhotoReference());
        }

        return point;
    }
}