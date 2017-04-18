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
            return;
        }
        loadPastTrips();
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

    @Override
    public void addTrip(String tripId) {
        mRepository.loadTrip(tripId, new DataSource.LoadTripCallback() {
            @Override
            public void onTripLoaded(Trip trip) {
                mTripsView.onTripAdded(trip);
            }

            @Override
            public void onFailure() {
                // TODO: add something here, maybe alert of add trip failure?
                // it wouldnt have failed though if we got here, so not sure how to handle
            }
        });
    }

    @Override
    public void reloadTripAfterEdit(String tripId) {
        mRepository.loadTrip(tripId, new DataSource.LoadTripCallback() {
            @Override
            public void onTripLoaded(Trip trip) {
                mTripsView.onTripEdited(trip);
            }

            @Override
            public void onFailure() {
                // TODO: add something here, maybe alert of edit trip failure?
                // it wouldnt have failed though if we got here, so not sure how to handle
            }
        });
    }

    private void loadUpcomingTrips() {
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

    private void loadPastTrips() {
        mRepository.loadPastTrips(new DataSource.LoadTripListCallback() {
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
}
