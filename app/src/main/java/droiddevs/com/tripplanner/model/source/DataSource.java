package droiddevs.com.tripplanner.model.source;

import android.location.Location;

import com.parse.ParseException;

import java.util.List;
import java.util.Set;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.fb.FbPlace;
import droiddevs.com.tripplanner.model.fb.FbUser;
import droiddevs.com.tripplanner.model.map.PlaceItem;

/**
 * Created by elmira on 4/3/17.
 */

public interface DataSource {

    interface LoadTripListCallback {
        void onTripListLoaded(List<Trip> trips);

        void onFailure();
    }

    interface LoadTripCallback {
        void onTripLoaded(Trip trip);

        void onFailure();
    }

    interface LoadDestinationCallback {
        void onDestinationLoaded(Destination destination);

        void onFailure();
    }

    interface SaveTripCallback {
        void onSuccess(Trip trip);

        void onFailed();
    }

    interface LoadFbUserCallback {
        void onUserLoaded(FbUser user) throws ParseException;


        void onFailure();
    }

    interface LoadPlaceCallback {
        void onPlaceLoaded(SavedPlace place);

        void onFailure();
    }

    interface DeleteTripCallback {
        void onTripDeleted();
    }

    interface SearchFbPlacesCallback {
        void onPlacesFound(List<FbPlace> places);

        void onFailure();
    }

    interface SearchGooglePlacesCallback {
        void onPlacesFound(List<PlaceItem> places);

        void onFailure();
    }

    interface PlaceDetailsCallback {
        void onPlacesDetailsLoaded(PlaceItem place);

        void onFailure();
    }

    interface LoadSavedPlacesCallback {
        void onSavedPlacesLoaded(List<SavedPlace> places);

        void onFailure();
    }

    interface LoadSavedPlacesIdsCallback {
        void onSavedPlacesIdsLoaded(Set<String> ids);
        void onFailure();
    }

    interface LoadSavedPlaceCallback {
        void onSavedPlaceLoaded(SavedPlace place);

        void onFailure();
    }

    interface DeleteSavedPlaceCallback {
        void onSuccess();

        void onFailed();
    }

    interface CreateSavedPlaceCallback {
        void onSuccess();

        void onFailed();
    }

    void loadUpcomingTrips(LoadTripListCallback callback);

    void loadPastTrips(LoadTripListCallback callback);

    void loadTrip(String tripId, LoadTripCallback callback);

    void updateTrip(Trip trip, SaveTripCallback callback);

    void updateTrip(Trip trip);

    void loadTripDestinations(Trip trip, LoadTripCallback callback);

    void loadDestination(String destinationId, LoadDestinationCallback callback);

    void loadCurrentFBUser(LoadFbUserCallback callback);

    void loadPlace(String placeId, LoadPlaceCallback callback);

    SavedPlace loadPlaceSynchronously(String placeId);

    void deleteTrip(Trip trip, DeleteTripCallback callback);

    void updateDestination(Destination destination);

    void searchFbPlaces(Location location, int radiusInMeters, int resultsLimit, String searchText, SearchFbPlacesCallback callback);

    void searchGooglePlaces(String location, String destinationId, int radiusInMeters, String searchTypeString, String apiKey, final SearchGooglePlacesCallback callback);

    void loadPlaceDetails(String placeId, String destinationId, PlaceDetailsCallback callback);

    void loadSavedPlaces(String destinationId, LoadSavedPlacesCallback callback);

    void loadSavedPlace(String googlePlaceId, String destinationId, LoadSavedPlaceCallback callback);

    void deleteSavedPlace(SavedPlace savedPlace, DeleteSavedPlaceCallback callback);

    void createSavedPlace(SavedPlace savedPlace, CreateSavedPlaceCallback callback);

    void loadSavedPlacesIds(String destinationId, LoadSavedPlacesIdsCallback callback);
}