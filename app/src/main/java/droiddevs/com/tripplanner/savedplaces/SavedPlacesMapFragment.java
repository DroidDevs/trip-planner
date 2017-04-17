package droiddevs.com.tripplanner.savedplaces;

import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.adapters.savedplacemap.SavedPlaceMapAdapter;
import droiddevs.com.tripplanner.map.BaseMapFragment;
import droiddevs.com.tripplanner.map.MapContract;
import droiddevs.com.tripplanner.model.map.PlaceItem;

/**
 * Created by elmira on 4/16/17.
 */

public class SavedPlacesMapFragment extends BaseMapFragment<PlaceItem> {

    private SavedPlacesContract.Presenter mPresenter;

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.reloadData();
    }

    public static SavedPlacesMapFragment newInstance() {
        SavedPlacesMapFragment fragment = new SavedPlacesMapFragment();
        return fragment;
    }

    @Override
    public void onLoadFailure() {

    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        //ignore this
    }

    public void setActivityPresenter(SavedPlacesContract.Presenter presenter){
        mPresenter = presenter;
    }

    @Override
    public BaseMapAdapter getMapAdapter() {
        return new SavedPlaceMapAdapter();
    }
}