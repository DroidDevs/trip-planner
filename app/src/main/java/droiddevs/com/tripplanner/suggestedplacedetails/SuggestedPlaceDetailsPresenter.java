package droiddevs.com.tripplanner.suggestedplacedetails;

import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;

/**
 * Created by Jared12 on 4/15/17.
 */

public class SuggestedPlaceDetailsPresenter implements SuggestedPlaceDetailsContract.Presenter {
    private GooglePlace mCurrentPlace;

    private SuggestedPlaceDetailsContract.View mView;
    private Repository mRepository;
    private SavedPlace mCurrentSavedPlace;

    private String mDestinationId;

    public SuggestedPlaceDetailsPresenter(Repository repository, SuggestedPlaceDetailsContract.View view, GooglePlace currentPlace, String destinationId) {
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
        SavedPlace newSavedPlace = SavedPlace.savedPlaceFromGooglePlace(place);
        newSavedPlace.setDestinationId(mDestinationId);
        mCurrentSavedPlace = newSavedPlace;

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
        } else {
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
