package droiddevs.com.tripplanner.addedittrip;

import com.google.android.gms.location.places.Place;

import java.util.Date;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 * Created by elmira on 4/4/17.
 */

public interface Contract {

    interface View extends BaseView<Presenter>{

        void setTripDetails(Trip trip);

        void onTripSaved();

        void onNewDestinationAdded(Destination destination);

        boolean isActive();
    }

    interface Presenter{

        void addNewDestinationPlace(Place googlePlace);

        void changeDestinationPlace(String destinationId, Place googlePlace);

        void setDuration(String placeId, int duration);

        void setTripName(String name);

        void setTripStartDate(Date startDate);

        void saveTrip();
    }
}
