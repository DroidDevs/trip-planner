package droiddevs.com.tripplanner.places;

import android.content.Intent;

import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.adapters.placemap.PlaceMapAdapter;
import droiddevs.com.tripplanner.map.BaseMapFragment;
import droiddevs.com.tripplanner.map.MapContract;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.placedetails.PlaceDetailsActivity;

/**
 * Created by elmira on 4/21/17.
 */

public class PlacesMapFragment extends BaseMapFragment<PlaceItem> {

    private MapContract.Presenter mPresenter;

    public static PlacesMapFragment newInstance() {
        return new PlacesMapFragment();
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
        PlaceMapAdapter adapter = new PlaceMapAdapter();
        adapter.setOnClickListener(this);
        return adapter;
    }

    @Override
    public void onMapItemClick(PlaceItem placeItem) {
        Intent intent = new Intent(getContext(), PlaceDetailsActivity.class);
        intent.putExtra(PlaceDetailsActivity.ARG_PLACE_OBJ, placeItem);
        intent.putExtra(PlaceDetailsActivity.ARG_DESTINATION_ID, placeItem.getDestinationId());
        startActivity(intent);
    }
}
