package droiddevs.com.tripplanner.triplist;

import android.support.annotation.NonNull;

import java.util.List;

import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;

/**
 * Created by jared.manfredi on 4/5/17.
 * Updated by Elmira Andreeva on 4/20/17
 */

public class TripsPresenter implements TripsContract.Presenter {
    private final Repository mRepository;
    private final TripsContract.View mTripsView;
    private boolean mPastEvents = false;

    public TripsPresenter(@NonNull Repository repository, @NonNull TripsContract.View tripsView, boolean pastEvents) {
        mRepository = repository;
        mTripsView = tripsView;
        mPastEvents = pastEvents;

        mTripsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTrips();
    }

    @Override
    public void loadTrips() {
        if (!mPastEvents) {
            loadUpcomingTrips();
        }
        else {
            loadPastTrips();
        }
    }

    @Override
    public void deleteTrip(final Trip trip) {
        mRepository.deleteTrip(trip, new Repository.DeleteTripCallback() {
            @Override
            public void onTripDeleted() {
                if (mTripsView != null && mTripsView.isActive()) {
                    mTripsView.onTripDeleted(trip.getTripId());
                }
            }
        });
    }

    private void loadUpcomingTrips() {
        if (mTripsView == null || !mTripsView.isActive()) return;

        mTripsView.setLoadingLayout(true);

        mRepository.loadUpcomingTrips(new DataSource.LoadTripListCallback() {
            @Override
            public void onTripListLoaded(List<Trip> trips) {
                if (mTripsView.isActive()) {
                    mTripsView.setLoadingLayout(false);
                    mTripsView.showTrips(trips);
                }
            }

            @Override
            public void onFailure() {
                mTripsView.setLoadingLayout(false);
                mTripsView.onFailure();
            }
        });
    }

    private void loadPastTrips() {
        mRepository.loadPastTrips(new DataSource.LoadTripListCallback() {
            @Override
            public void onTripListLoaded(List<Trip> trips) {
                if (mTripsView != null && mTripsView.isActive()) {
                    mTripsView.showTrips(trips);
                }
            }

            @Override
            public void onFailure() {
                mTripsView.onFailure();
            }
        });
    }
}
