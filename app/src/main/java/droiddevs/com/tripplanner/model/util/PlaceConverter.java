package droiddevs.com.tripplanner.model.util;

import java.util.ArrayList;
import java.util.List;

import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.model.source.remote.PlaceDetailsResponse;

/**
 * Created by elmira on 4/13/17.
 */

public class PlaceConverter {

    public static SavedPlace convertToSavedPlace(PlaceDetailsResponse response) {

        PlaceDetailsResponse.PlaceDetails placeDetails = response.getPlaceDetails();
        SavedPlace point = new SavedPlace();

        point.setPlaceId(placeDetails.getPlaceId());
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

    public static List<PlaceItem> convertToPlaceItemListFromSavedPlace(List<SavedPlace> savedPlaces) {
        if (savedPlaces == null) return new ArrayList<>();
        List<PlaceItem> list = new ArrayList<>();
        for (int i = 0; i < savedPlaces.size(); i++) {
            SavedPlace savedPlace = savedPlaces.get(i);
            list.add(new PlaceItem(savedPlace, i));
        }
        return list;
    }

    public static List<PlaceItem> convertToPlaceItemListFromGooglePlace(List<GooglePlace> googlePlaces) {
        if (googlePlaces == null) return new ArrayList<>();
        List<PlaceItem> list = new ArrayList<>();
        for (int i = 0; i < googlePlaces.size(); i++) {
            GooglePlace googlePlace = googlePlaces.get(i);
            list.add(new PlaceItem(googlePlace, i, null, false));
        }
        return list;
    }

    public static SavedPlace convertToSavedPlace(String destinationId, GooglePlace googlePlace) {
        if (googlePlace == null) return null;
        SavedPlace savedPlace = new SavedPlace();
        savedPlace.setDestinationId(destinationId);

        savedPlace.setName(googlePlace.getName());
        savedPlace.setPlaceId(googlePlace.getPlaceId());

        savedPlace.setLatitude(googlePlace.getGeometry().getLocation().getLat());
        savedPlace.setLongitude(googlePlace.getGeometry().getLocation().getLng());

        savedPlace.setRating(googlePlace.getRating());

        String photoReference = null;
        if (googlePlace.getPhotos() != null && googlePlace.getPhotos().size() > 0) {
            photoReference = googlePlace.getPhotos().get(0).getPhotoReference();
        }

        savedPlace.setPhotoReference(photoReference);
        //savedPlace.setPhoneNumber(googlePlace.get);
        savedPlace.setTypes(googlePlace.getTypes());

        return savedPlace;
    }
}