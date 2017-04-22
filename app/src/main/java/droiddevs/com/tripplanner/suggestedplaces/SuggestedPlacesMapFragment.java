package droiddevs.com.tripplanner.suggestedplaces;

import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.adapters.placemap.PlaceMapAdapter;
import droiddevs.com.tripplanner.map.BaseMapFragment;
import droiddevs.com.tripplanner.map.MapContract;
import droiddevs.com.tripplanner.model.map.PlaceItem;

/**
 * Created by elmira on 4/21/17.
 */

public class SuggestedPlacesMapFragment  extends BaseMapFragment<PlaceItem> {

    private MapContract.Presenter mPresenter;

    public static SuggestedPlacesMapFragment newInstance(){
        return new SuggestedPlacesMapFragment();
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
