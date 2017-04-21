package droiddevs.com.tripplanner.triplist;

import android.content.Context;

import java.util.List;

import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.mvp.BasePresenter;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 * Created by jared.manfredi on 4/5/17.
 * Updated by Elmira Andreeva on 4/20/17
 */

public interface TripsContract {
    interface View extends BaseView<Presenter> {
        void showTrips(List<Trip> trips);

        void onTripDeleted(String tripId);

        boolean isActive();

        void setLoadingLayout(boolean isLoading);
    }

    interface Presenter extends BasePresenter {
        void loadTrips();

        void deleteTrip(Trip trip);
    }
}