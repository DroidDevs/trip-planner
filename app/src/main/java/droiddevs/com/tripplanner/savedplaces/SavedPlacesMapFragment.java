package droiddevs.com.tripplanner.savedplaces;

import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.adapters.placemap.PlaceMapAdapter;
import droiddevs.com.tripplanner.map.BaseMapFragment;
import droiddevs.com.tripplanner.map.MapContract;
import droiddevs.com.tripplanner.model.map.PlaceItem;

/**
 * Created by elmira on 4/16/17.
 */

public class SavedPlacesMapFragment extends BaseMapFragment<PlaceItem> {

    private MapContract.Presenter mPresenter;

    public static SavedPlacesMapFragment newInstance() {
        return new SavedPlacesMapFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.reloadData();
    }

    @Override
    public void onLoadFailure() {

    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public BaseMapAdapter getMapAdapter() {
        return new PlaceMapAdapter();
    }
}