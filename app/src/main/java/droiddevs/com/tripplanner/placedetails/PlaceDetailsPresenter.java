package droiddevs.com.tripplanner.placedetails;

import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;
import droiddevs.com.tripplanner.model.util.PlaceConverter;

/**
 * Updated by Elmira Andreeva on 4/24/2017
 * Created by Jared12 on 4/15/17.
 */

public class PlaceDetailsPresenter implements PlaceDetailsContract.Presenter {

    private PlaceDetailsContract.View mView;
    private Repository mRepository;

    private SavedPlace mCurrentSavedPlace;
    private PlaceItem mCurrentPlace;

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
        if (mCurrentPlace == null) return;

        mRepository.loadPlaceDetails(mCurrentPlace.getPlaceId(), mDestinationId, new DataSource.PlaceDetailsCallback() {
            @Override
            public void onPlacesDetailsLoaded(PlaceItem place) {
                if (place != null) {
                    mCurrentPlace = place;
                }
                mRepository.loadSavedPlace(mCurrentPlace.getPlaceId(), mDestinationId, new DataSource.LoadSavedPlaceCallback() {
                    @Override
                    public void onSavedPlaceLoaded(SavedPlace place) {
                        if (mView == null || !mView.isActive()) return;

                        if (place != null) {
                            mCurrentSavedPlace = place;
                            mCurrentPlace.setSaved(true);
                        }
                        else {
                            mCurrentPlace.setSaved(false);
                        }
                        mView.showPlaceDetails(mCurrentPlace);
                    }

                    @Override
                    public void onFailure() {
                        if (mView == null || !mView.isActive()) return;

                        mCurrentPlace.setSaved(false);
                        mView.showPlaceDetails(mCurrentPlace);
                    }
                });
            }

            @Override
            public void onFailure() {
                if (mView == null || !mView.isActive()) return;

                if (mCurrentPlace == null) {
                    mView.onFailure();
                }
                else {
                    mView.showPlaceDetails(mCurrentPlace);
                }
            }
        });
    }

    @Override
    public void savePlace() {
        if (mCurrentPlace != null) {
            SavedPlace newSavedPlace = PlaceConverter.convertToSavedPlaceFromPlaceItem(mCurrentPlace);
            mRepository.createSavedPlace(newSavedPlace, null);
            mCurrentSavedPlace = newSavedPlace;
        }
    }

    @Override
    public void deletePlace() {
        if (mCurrentSavedPlace != null) {
            deleteSavedPlace(mCurrentSavedPlace);
            mCurrentSavedPlace = null;
        }
    }

    @Override
    public PlaceItem getCurrentPlace() {
        return mCurrentPlace;
    }

    private void deleteSavedPlace(SavedPlace place) {
        mRepository.deleteSavedPlace(place, null);
    }
}
