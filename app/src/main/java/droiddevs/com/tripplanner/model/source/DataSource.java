package droiddevs.com.tripplanner.model.source;

import java.util.List;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.FbUser;
import droiddevs.com.tripplanner.model.Trip;

/**
 * Created by elmira on 4/3/17.
 */

public interface DataSource {

    interface LoadTripListCallback {
        void onTripListLoaded(List<Trip> trips);

        void onFailure();
    }

    interface LoadTripCallback {
        void onTripLoaded(Trip trip);

        void onFailure();
    }

    interface LoadDestinationCallback {
        void onDestinationLoaded(Destination destination);

        void onFailure();
    }

    interface SaveTripCallback {
        void onSuccess();

        void onFailed();
    }

    interface LoadFbUserCallback {
        void onUserLoaded(FbUser user);
        void onFailure();
    }

    void loadOpenTrips(LoadTripListCallback callback);

    void loadTrip(String tripId, LoadTripCallback callback);

    void updateTrip(Trip trip, SaveTripCallback callback);

    void updateTrip(Trip trip);

    void loadTripDestinations(Trip trip, LoadTripCallback callback);

    void loadCurrentFBUser(LoadFbUserCallback callback);

}
