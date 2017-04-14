package droiddevs.com.tripplanner.tripmap;

import android.os.Bundle;
import android.support.annotation.Nullable;

import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.adapters.tripmap.TripMapAdapter;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.map.BaseMapFragment;
import droiddevs.com.tripplanner.map.MapContract;

/**
 * Created by elmira on 4/11/17.
 */

public class TripMapFragment extends BaseMapFragment {

    public static final String ARGUMENT_TRIP_ID = "tripId";
    private MapContract.Presenter mPresenter;
    private String mTripId;

    public static TripMapFragment newInstance(String tripId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_TRIP_ID, tripId);

        TripMapFragment fragment = new TripMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTripId = getArguments().getString(ARGUMENT_TRIP_ID);

        mPresenter = new TripMapPresenter(this, TripPlannerApplication.getRepository(), mTripId);

        mPresenter.start();
    }

    @Override
    public BaseMapAdapter getMapAdapter() {
        return new TripMapAdapter();
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
        this.mPresenter = presenter;
    }
}
