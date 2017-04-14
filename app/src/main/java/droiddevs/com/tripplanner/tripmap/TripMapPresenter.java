package droiddevs.com.tripplanner.tripmap;

import java.util.ArrayList;
import java.util.List;

import droiddevs.com.tripplanner.map.MapContract;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.map.TripDestinationMapItem;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;

/**
 * Created by elmira on 4/11/17.
 */

public class TripMapPresenter implements MapContract.Presenter {

    private MapContract.View mView;
    private Repository mRepository;
    private String mTripId;

    public TripMapPresenter(MapContract.View mView, Repository repository, String tripId) {
        this.mView = mView;
        this.mRepository = repository;
        this.mView.setPresenter(this);
        this.mTripId = tripId;
    }

    @Override
    public void start() {
        mRepository.loadTrip(mTripId, new DataSource.LoadTripCallback() {
            @Override
            public void onTripLoaded(Trip trip) {
                if (trip.getDestinations() != null && trip.getDestinations().size() > 0) {
                    List<TripDestinationMapItem> mapData = new ArrayList<TripDestinationMapItem>();
                    for (int i = 0; i < trip.getDestinations().size(); i++) {
                        Destination destination = trip.getDestinations().get(i);
                        mapData.add(new TripDestinationMapItem(destination, i));
                    }
                    if (mView != null && mView.isActive()) {
                        mView.setMapData(mapData);
                    }
                }
            }

            @Override
            public void onFailure() {
                if (mView != null && mView.isActive()) {
                    mView.onLoadFailure();
                }
            }
        });
    }
}