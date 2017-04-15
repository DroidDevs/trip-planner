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
            int targetWidth = 1600;
            int targetHeight = 1000;
            int maxWidth = 0;
            PlaceDetailsResponse.PlacePhoto maxWidthPhoto = null, targetPhoto = null;

            for (PlaceDetailsResponse.PlacePhoto photo : placeDetails.getPhotos()) {
                if (photo.getWidth() > targetWidth && photo.getWidth() > targetHeight) {
                    targetPhoto = photo;
                }
                else if (photo.getWidth() > maxWidth && photo.getHeight() > targetHeight) {
                    maxWidth = photo.getWidth();
                    maxWidthPhoto = photo;
                }
            }
            if (maxWidthPhoto != null) {
                point.setPhotoReference(maxWidthPhoto.getPhotoReference());
            }
            else if (targetPhoto != null) {
                point.setPhotoReference(targetPhoto.getPhotoReference());
            }
            else {
                point.setPhotoReference(placeDetails.getPhotos().get(0).getPhotoReference());
            }
        }
        return point;
    }
}