package droiddevs.com.tripplanner.savedplaces;

import java.util.List;

import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;
import droiddevs.com.tripplanner.model.util.PlaceConverter;
import droiddevs.com.tripplanner.util.NetworkUtil;

/**
 * Created by elmira on 4/16/17.
 */

public class SavedPlacesPresenter implements SavedPlacesContract.Presenter {

    private Repository mRepository;
    private SavedPlacesContract.View mView;
    private String mDestinationId;

    private boolean isLoading = false;
    private List<PlaceItem> mSavedPlaces;

    public SavedPlacesPresenter(Repository mRepository, SavedPlacesContract.View mView, String mDestinationId) {
        this.mRepository = mRepository;
        this.mView = mView;
        this.mDestinationId = mDestinationId;
    }

    @Override
    public void start() {
        if (mView==null) return;

        mRepository.loadSavedPlaces(mDestinationId, new DataSource.LoadSavedPlacesCallback() {
            @Override
            public void onSavedPlacesLoaded(List<SavedPlace> places) {
                mSavedPlaces = PlaceConverter.convertToPlaceItemListFromSavedPlace(places);
                if (mView != null) {
                    mView.onPlacesLoaded(mSavedPlaces);
                }
            }

            @Override
            public void onFailure() {
                if (mView != null) {
                    mView.onFailure();
                }
            }
        });
    }

    @Override
    public void reloadData() {
        if (isLoading) return;
        if (mSavedPlaces == null || mSavedPlaces.size() == 0) {
            start();
        }
        else {
            if (mView != null) {
                mView.onPlacesLoaded(mSavedPlaces);
            }
        }
    }
}
