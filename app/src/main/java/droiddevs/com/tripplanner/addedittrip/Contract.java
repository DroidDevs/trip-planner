package droiddevs.com.tripplanner.addedittrip;

import android.content.Context;

import com.google.android.gms.location.places.Place;

import java.util.Date;
import java.util.List;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.mvp.BasePresenter;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 * Created by elmira on 4/4/17.
 */

public interface Contract {

    interface View extends BaseView<Presenter> {

        void setTripDetails(Trip trip);

        void onTripSaved();

        void onTripSaveFailure();

        boolean isActive();

        void onDestinationAdded(Destination destination);

        void onDestinationChanged(String oldDestId, Destination destination);

        void onDestinationRemoved(String oldDestId);

        void onTripLoadFailure();

        void onTripSaveErrorEmptyName();

        void onTripSaveErrorEmptyDestination();

        void onTripEndDateChanged(Date endDate);

        Context getContext();
    }

    interface Presenter extends BasePresenter {

        void addNewDestinationPlace(Place googlePlace);

        void changeDestinationPlace(String destinationId, Place googlePlace);

        void removeDestination(String destinationId);

        void changeTripStartDate(Date startDate);

        void saveTrip(String name, Date startDate, List<String> destinationOrder);

        void createTripFromPlace(Place googlePlace);
    }
}
