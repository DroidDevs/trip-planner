package droiddevs.com.tripplanner.triplist;

import android.support.annotation.NonNull;

import java.util.List;

import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;

/**
 * Created by jared.manfredi on 4/5/17.
 */

public class TripsPresenter implements TripsContract.Presenter {
    private final Repository mRepository;
    private final TripsContract.View mTripsView;

    public TripsPresenter(@NonNull Repository repository, @NonNull TripsContract.View tripsView) {
        mRepository = repository;
        mTripsView = tripsView;

        mTripsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTrips();
    }

    @Override
    public void loadTrips() {
        mRepository.loadOpenTrips(new DataSource.LoadTripListCallback() {
            @Override
            public void onTripListLoaded(List<Trip> trips) {
                mTripsView.showTrips(trips);
            }

            @Override
            public void onFailure() {
                // TODO: SHOW FAILURE TO LOAD TRIPS
            }
        });
    }

    @Override
    public void deleteTrip(Trip trip, final int position) {
        mRepository.deleteTrip(trip, new Repository.DeleteTripCallback() {
            @Override
            public void onTripDeleted() {
                mTripsView.onTripDeleted(position);
            }
        });
    }
}
