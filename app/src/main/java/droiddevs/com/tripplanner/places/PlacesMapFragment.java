package droiddevs.com.tripplanner.places;

import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.adapters.placemap.PlaceMapAdapter;
import droiddevs.com.tripplanner.map.BaseMapFragment;
import droiddevs.com.tripplanner.map.MapContract;
import droiddevs.com.tripplanner.model.map.PlaceItem;

/**
 * Created by elmira on 4/21/17.
 */

public class PlacesMapFragment extends BaseMapFragment<PlaceItem> {

    private MapContract.Presenter mPresenter;

    public static PlacesMapFragment newInstance(){
        return new PlacesMapFragment();
    }

    @Override
    public void onLoadFailure() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.reloadData();
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
       this.mPresenter = presenter;
    }

    @Override
    public BaseMapAdapter getMapAdapter() {
        return new PlaceMapAdapter();
    }
}
