package droiddevs.com.tripplanner.places;

import android.util.Log;

import com.parse.ParseException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

public class PlacesPresenter implements PlacesContract.Presenter {

    private static String TAG = PlacesPresenter.class.getSimpleName();

    private String mSearchString;
    private String mDestinationId;

    private PlacesContract.View mView;
    private Repository mRepository;

    private List<PlaceItem> mSuggestedPlaces;
    private Set<String> mSavedPlaceIds;

    private boolean isLoading = false;

    public PlacesPresenter(Repository repository, PlacesContract.View view, String placeTypeSearchString, String destinationId) {
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
    public void reloadData() {
        if (mSuggestedPlaces != null
                && mSuggestedPlaces.size() > 0) {
            if (mView != null) {
                mView.showSuggestedPlaces(mSuggestedPlaces, mSavedPlaceIds);
            }
        }
        else start();
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

    private void loadSuggestedPlacesForDestination(final Destination destination, String placeTypeSearchString) {
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
                        mSuggestedPlaces = places;

                        mRepository.loadSavedPlacesIds(destination.getDestinationId(), new DataSource.LoadSavedPlacesIdsCallback() {
                            @Override
                            public void onSavedPlacesIdsLoaded(Set<String> ids) {
                                mSavedPlaceIds = ids;
                                mView.showSuggestedPlaces(mSuggestedPlaces, ids);
                            }

                            @Override
                            public void onFailure() {
                                mView.showSuggestedPlaces(mSuggestedPlaces, new HashSet<String>());
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        mView.showSuggestedPlaces(null, null);
                    }
                });
    }

    private void loadSavedPlaces(Destination destination) {
        mRepository.loadSavedPlaces(destination.getDestinationId(), new DataSource.LoadSavedPlacesCallback() {
            @Override
            public void onSavedPlacesLoaded(List<SavedPlace> places) {
                List<PlaceItem> placeItems = PlaceConverter.convertToPlaceItemListFromSavedPlace(places);
                mSuggestedPlaces = placeItems;
                mSavedPlaceIds = convertToIdSet(placeItems);
                mView.showSuggestedPlaces(placeItems, mSavedPlaceIds);
            }

            @Override
            public void onFailure() {
                mView.showSuggestedPlaces(null, null);
            }
        });
    }

    private Set<String> convertToIdSet(List<PlaceItem> places) {
        Set<String> idSet = new HashSet<>();
        for (PlaceItem place : places) {
            idSet.add(place.getPlaceId());
        }
        return idSet;
    }

    @Override
    public void savePlace(PlaceItem placeItem) {
        SavedPlace savedPlace = PlaceConverter.convertToSavedPlaceFromPlaceItem(placeItem);
        try {
            mSavedPlaceIds.add(placeItem.getPlaceId());
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
                    mSavedPlaceIds.remove(savedPlace.getPlaceId());
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
