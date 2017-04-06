package droiddevs.com.tripplanner.addedittrip;

import com.google.android.gms.location.places.Place;

import java.util.Date;

import droiddevs.com.tripplanner.model.source.Repository;

/**
 * Created by elmira on 4/4/17.
 */

public class AddEditTripPresenter implements Contract.Presenter {

    private Repository mRepository;
    private Contract.View mView;

    public AddEditTripPresenter(Repository mRepository, Contract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
    }

    @Override
    public void addNewDestinationPlace(Place googlePlace) {

    }

    @Override
    public void changeDestinationPlace(String destinationId, Place googlePlace) {

    }

    @Override
    public void setDuration(String placeId, int duration) {

    }

    @Override
    public void setTripName(String name) {

    }

    @Override
    public void setTripStartDate(Date startDate) {

    }

    @Override
    public void saveTrip() {

    }
}