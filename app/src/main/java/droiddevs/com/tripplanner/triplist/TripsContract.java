package droiddevs.com.tripplanner.triplist;

import java.util.List;

import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.mvp.BasePresenter;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 * Created by jared.manfredi on 4/5/17.
 */

public interface TripsContract {
    interface View extends BaseView<Presenter> {
        void showTrips(List<Trip> trips);

        void onTripDeleted(int position);

        void onTripAdded(Trip trip);

        void onTripEdited(Trip trip);
    }

    interface Presenter extends BasePresenter {
        void loadTrips();

        void deleteTrip(Trip trip, final int position);

        void addTrip(String tripId);

        void reloadTripAfterEdit(String tripId);
    }
}