package droiddevs.com.tripplanner.suggestedplaces;

import android.util.Log;

import com.parse.ParseException;

import java.util.List;

import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.PlaceOption;
import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;
import droiddevs.com.tripplanner.model.util.PlaceConverter;

/**
 * Created by Jared12 on 4/15/17.
 */

public class SuggestedPlacesPresenter implements SuggestedPlacesContract.Presenter {

    private static String TAG = SuggestedPlacesPresenter.class.getSimpleName();

    private String mSearchString;
    private String mDestinationId;

    private SuggestedPlacesContract.View mView;
    private Repository mRepository;

    public SuggestedPlacesPresenter(Repository repository, SuggestedPlacesContract.View view, String placeTypeSearchString, String destinationId) {
        mSearchString = placeTypeSearchString;
        mDestinationId = destinationId;

        mView = view;
        mRepository = repository;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadSuggestedPlaces(mSearchString, mDestinationId);
    }

    @Override
    public void loadSuggestedPlaces(String placeTypeSearchString, String destinationId) {
        // Load destination before places so we can pass destination as location
        loadDestinationFromId(destinationId, placeTypeSearchString);
    }

    private void loadDestinationFromId(String destinationId, final String placeTypeSearchString) {
        mRepository.loadDestination(destinationId, new DataSource.LoadDestinationCallback() {
            @Override
            public void onDestinationLoaded(Destination destination) {
                // Load suggested places for destination
                loadSuggestedPlacesForDestination(destination, placeTypeSearchString);
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "Error loading destination for sugg");
            }
        });
    }

    private void loadSuggestedPlacesForDestination(Destination destination, String placeTypeSearchString) {
        if (placeTypeSearchString.equals(
                PlaceOption.PlaceOptionType.TYPE_SAVED_PLACES.typeSearchString())) {
            loadSavedPlaces(destination);
            return;
        }

        final String locationString = String.valueOf(destination.getLatitude())
                + "," + String.valueOf(destination.getLongitude());

        mRepository.searchGooglePlaces(locationString, destination.getDestinationId(), 10000, placeTypeSearchString, TripPlannerApplication.getGooglePlacesApiKey(),
                new DataSource.SearchGooglePlacesCallback() {
                    @Override
                    public void onPlacesFound(List<PlaceItem> places) {
                        mView.showSuggestedPlaces(places);
                    }

                    @Override
                    public void onFailure() {
                        mView.showSuggestedPlaces(null);
                    }
                });
    }

    private void loadSavedPlaces(Destination destination) {
        mRepository.loadSavedPlaces(destination.getDestinationId(), new DataSource.LoadSavedPlacesCallback() {
            @Override
            public void onSavedPlacesLoaded(List<SavedPlace> places) {
                List<PlaceItem> placeItems = PlaceConverter.convertToPlaceItemListFromSavedPlace(places);
                mView.showSuggestedPlaces(placeItems);
            }

            @Override
            public void onFailure() {
                mView.showSuggestedPlaces(null);
            }
        });
    }

    @Override
    public void savePlace(PlaceItem placeItem) {
        SavedPlace savedPlace = PlaceConverter.convertToSavedPlaceFromPlaceItem(placeItem);
        try {
            savedPlace.save();
        } catch (ParseException e) {
            Log.e(TAG, "Error saving place: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void deletePlace(PlaceItem placeItem) {
        // Load saved place object from db
        mRepository.loadSavedPlace(placeItem.getPlaceId(), placeItem.getDestinationId(), new DataSource.LoadSavedPlaceCallback() {
            @Override
            public void onSavedPlaceLoaded(SavedPlace place) {
                // Delete db object
                deleteSavedPlace(place);
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "Error loading saved place for delete");
            }
        });
    }

    private void deleteSavedPlace(final SavedPlace savedPlace) {
        mRepository.deleteSavedPlace(savedPlace, new DataSource.DeleteSavedPlaceCallback() {
            @Override
            public void onSuccess() {
                if (PlaceOption.PlaceOptionType.fromString(mSearchString)
                        == PlaceOption.PlaceOptionType.TYPE_SAVED_PLACES) {
                    mView.onSavedPlaceDeleted(new PlaceItem(savedPlace, 0));
                }
            }

            @Override
            public void onFailed() {
                Log.e(TAG, "Error deleting place");
            }
        });
    }
}
