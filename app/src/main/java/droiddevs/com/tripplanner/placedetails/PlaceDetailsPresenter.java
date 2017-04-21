package droiddevs.com.tripplanner.placedetails;

import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;
import droiddevs.com.tripplanner.model.util.PlaceConverter;

/**
 * Created by Jared12 on 4/15/17.
 */

public class PlaceDetailsPresenter implements PlaceDetailsContract.Presenter {
    private PlaceItem mCurrentPlace;

    private PlaceDetailsContract.View mView;
    private Repository mRepository;
    private SavedPlace mCurrentSavedPlace;

    private String mDestinationId;

    public PlaceDetailsPresenter(Repository repository, PlaceDetailsContract.View view, PlaceItem currentPlace, String destinationId) {
        mCurrentPlace = currentPlace;
        mDestinationId = destinationId;

        mView = view;
        mRepository = repository;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mRepository.loadSavedPlace(mCurrentPlace.getPlaceId(), mDestinationId, new DataSource.LoadSavedPlaceCallback() {
            @Override
            public void onSavedPlaceLoaded(SavedPlace place) {
                mCurrentSavedPlace = place;
                if (place != null) {
                    mView.showPlaceDetails(mCurrentPlace, true);
                    return;
                }
                mView.showPlaceDetails(mCurrentPlace, false);
            }

            @Override
            public void onFailure() {
                mView.showPlaceDetails(mCurrentPlace, false);
            }
        });
    }

    @Override
    public void savePlace(GooglePlace place) {
        SavedPlace newSavedPlace = PlaceConverter.convertToSavedPlaceFromGooglePlace(mDestinationId, place);

        mRepository.createSavedPlace(newSavedPlace,
                new DataSource.CreateSavedPlaceCallback() {
                    @Override
                    public void onSuccess() {
                        mView.onPlaceSaved(true);
                    }

                    @Override
                    public void onFailed() {
                        mView.onPlaceSaved(false);
                    }
                });
    }

    @Override
    public void deletePlace(GooglePlace place) {
        if (mCurrentSavedPlace != null) {
            deleteSavedPlace(mCurrentSavedPlace);
        }
        else {
            mView.onPlaceDeleted(false);
        }
    }

    private void deleteSavedPlace(SavedPlace place) {
        mRepository.deleteSavedPlace(place, new DataSource.DeleteSavedPlaceCallback() {
            @Override
            public void onSuccess() {
                mView.onPlaceDeleted(true);
            }

            @Override
            public void onFailed() {
                mView.onPlaceDeleted(false);
            }
        });
    }
}
