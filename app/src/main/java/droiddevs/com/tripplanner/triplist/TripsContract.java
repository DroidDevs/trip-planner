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
    }

    interface Presenter extends BasePresenter {
        void loadTrips();
        void deleteTrip(Trip trip, final int position);
    }
}