package droiddevs.com.tripplanner.model.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.model.googleplaces.OpeningHours;
import droiddevs.com.tripplanner.model.googleplaces.Photo;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.model.source.remote.PlaceDetailsResponse;

/**
 * Created by elmira on 4/13/17.
 */

public class PlaceConverter {

    private static final String LOG_TAG = "PlaceConverter";

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

    public static List<PlaceItem> convertToPlaceItemListFromGooglePlace(String destinationId, List<GooglePlace> googlePlaces) {
        if (googlePlaces == null) return new ArrayList<>();
        List<PlaceItem> list = new ArrayList<>();
        for (int i = 0; i < googlePlaces.size(); i++) {
            GooglePlace googlePlace = googlePlaces.get(i);
            List<Photo> photos = googlePlace.getPhotos();
            Photo placePhoto = null;
            if (photos.size() > 0) {
                placePhoto = photos.get(0);
            }
            //// TODO: check if this place is already saved or not?
            list.add(new PlaceItem(googlePlace, destinationId, i, placePhoto == null ? null : placePhoto.getFullPhotoURLReference(), false));
        }
        return list;
    }

    public static SavedPlace convertToSavedPlaceFromGooglePlace(String destinationId, GooglePlace place) {
        Log.d(LOG_TAG, "convertToSavedPlaceFromGooglePlace(), destId: " + destinationId);
        //todo Jared check why destinationId is null sometimes

        SavedPlace savedPlace = new SavedPlace();
        savedPlace.setDestinationId(destinationId);

        savedPlace.setName(place.getName());
        savedPlace.setPlaceId(place.getPlaceId());
        savedPlace.setLatitude(place.getGeometry().getLocation().getLat());
        savedPlace.setLongitude(place.getGeometry().getLocation().getLng());
        savedPlace.setRating(place.getRating());
        savedPlace.setTypes(place.getTypes());

        OpeningHours openingHours = place.getOpeningHours();
        if (openingHours != null) {
            savedPlace.setOpenNow(openingHours.getOpenNow());

            List<Object> weekdayHours = openingHours.getWeekdayText();
            if (weekdayHours != null) {
                savedPlace.setOpenNowWeekdayText(weekdayHours);
            }
        }

        List<Photo> photos = place.getPhotos();
        if (photos != null && photos.size() > 0) {
            Photo photo = photos.get(0);
            savedPlace.setPhotoReference(photo.getPhotoReference());
        }
        return savedPlace;
    }

    public static SavedPlace convertToSavedPlaceFromPlaceItem(PlaceItem placeItem) {
        SavedPlace savedPlace = new SavedPlace();
        savedPlace.setDestinationId(placeItem.getDestinationId());

        savedPlace.setName(placeItem.getName());
        savedPlace.setPlaceId(placeItem.getPlaceId());
        savedPlace.setLatitude(placeItem.getLatLng().latitude);
        savedPlace.setLongitude(placeItem.getLatLng().longitude);
        savedPlace.setRating(placeItem.getRating());

        if (placeItem.getWeekdayHours() != null) {
            savedPlace.setOpenNowWeekdayText(placeItem.getWeekdayHours());
        }
        if (placeItem.getPhotoReference() != null) {
            savedPlace.setPhotoReference(placeItem.getPhotoReference());
        }

        //savedPlace.setTypes(place.getTypes());
        return savedPlace;
    }
}