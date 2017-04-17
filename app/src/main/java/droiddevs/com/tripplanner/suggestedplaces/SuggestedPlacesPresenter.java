package droiddevs.com.tripplanner.suggestedplaces;

import java.util.List;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;

/**
 * Created by Jared12 on 4/15/17.
 */

public class SuggestedPlacesPresenter implements SuggestedPlacesContract.Presenter {
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
        loadDestinationFromId(destinationId, placeTypeSearchString);
    }

    private void loadDestinationFromId(String destinationId, final String placeTypeSearchString) {
        mRepository.loadDestination(destinationId, new DataSource.LoadDestinationCallback() {
            @Override
            public void onDestinationLoaded(Destination destination) {
                loadSuggestedPlacesForDestination(destination, placeTypeSearchString);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void loadSuggestedPlacesForDestination(Destination destination, String placeTypeSearchString) {
        final String GoogleAPIKey = "AIzaSyBLM-VesFSnqP6wvQJFB0pxOmMX4QDHHig";
        final String locationString = String.valueOf(destination.getLatitude())
                + "," + String.valueOf(destination.getLongitude());

        mRepository.searchGooglePlaces(locationString, 3219, placeTypeSearchString, GoogleAPIKey,
                new DataSource.SearchGooglePlacesCallback() {
                        @Override
                        public void onPlacesFound(List<GooglePlace> places) {
                            mView.showSuggestedPlaces(places);
                        }

                        @Override
                        public void onFailure() {
                            mView.showSuggestedPlaces(null);
                        }
        });
    }
}
