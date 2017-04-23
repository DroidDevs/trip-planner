package droiddevs.com.tripplanner.placedetails;

import droiddevs.com.tripplanner.model.SavedPlace;
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
        mRepository.loadPlaceDetails(mCurrentPlace.getPlaceId(), mDestinationId, new DataSource.PlaceDetailsCallback() {
            @Override
            public void onPlacesDetailsLoaded(PlaceItem place) {
                if (place != null) {
                    mCurrentPlace = place;
                }
                loadSavedPlace();
            }

            @Override
            public void onFailure() {
                loadSavedPlace();
            }
        });
    }

    private void loadSavedPlace() {
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
    public void savePlace(PlaceItem place) {
        SavedPlace newSavedPlace = PlaceConverter.convertToSavedPlaceFromPlaceItem(place);
        mRepository.createSavedPlace(newSavedPlace, null);
    }

    @Override
    public void deletePlace(PlaceItem place) {
        if (mCurrentSavedPlace != null) {
            deleteSavedPlace(mCurrentSavedPlace);
        }
    }

    private void deleteSavedPlace(SavedPlace place) {
        mRepository.deleteSavedPlace(place, null);
    }
}
