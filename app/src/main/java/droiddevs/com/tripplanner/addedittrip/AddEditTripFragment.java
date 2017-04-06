package droiddevs.com.tripplanner.addedittrip;

import android.support.v4.app.Fragment;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;

/**
 * Created by elmira on 4/4/17.
 */

public class AddEditTripFragment extends Fragment implements Contract.View {

    private Contract.Presenter mPresenter;

    public AddEditTripFragment(){

    }

    public static AddEditTripFragment newInstance(){
        AddEditTripFragment fragment = new AddEditTripFragment();
        return fragment;
    }

    @Override
    public void setTripDetails(Trip trip) {

    }

    @Override
    public void onTripSaved() {

    }

    @Override
    public void onNewDestinationAdded(Destination destination) {

    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
