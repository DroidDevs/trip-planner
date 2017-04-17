package droiddevs.com.tripplanner.addedittrip;

import android.util.Log;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;
import droiddevs.com.tripplanner.util.NetworkUtil;

/**
 * Created by elmira on 4/4/17.
 */

public class AddEditTripPresenter implements Contract.Presenter {

    private static final String LOG_TAG = "AddEditTripPresenter";

    private Repository mRepository;
    private Contract.View mView;

    private Trip mTrip;
    private String mTripId;

    private Calendar mCalendar = Calendar.getInstance();
    private static final int DEFAULT_DURATION_IN_DAYS = 2;

    /*//todo remove this
    private List<SavedPlace> savedPlaces = new ArrayList<>();*/

    public AddEditTripPresenter(Repository mRepository, Contract.View mView, String tripId) {
        this.mRepository = mRepository;
        this.mView = mView;
        this.mTripId = tripId;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        Log.d(LOG_TAG, " start() tripId: " + mTripId);
        if (mView == null) return;

        if (!isNewTrip()) {
            mRepository.setCanLoadFromRemoteSource(NetworkUtil.isNetworkAvailable(mView.getContext()));
            mRepository.loadTrip(mTripId, new DataSource.LoadTripCallback() {
                @Override
                public void onTripLoaded(Trip trip) {
                    mTrip = trip;
                    if (mView != null && mView.isActive()) {
                        mView.setTripDetails(trip);
                    }
                }

                @Override
                public void onFailure() {
                    if (mView != null && mView.isActive()) {
                        mView.onTripLoadFailure();
                    }
                }
            });
        }
    }

    public void destroy() {
        mView = null;
    }

    @Override
    public void addNewDestinationPlace(Place googlePlace) {
        if (mView == null || !mView.isActive()) return;

        Destination destination = convertPlaceToDestination(googlePlace);
        destination.setTripId(mTrip.getTripId());
        destination.setDuration(DEFAULT_DURATION_IN_DAYS);

        List<Destination> destinations = mTrip.getDestinations();
        if (destinations == null) {
            destinations = new ArrayList<>();
        }

        mCalendar.setTime(mTrip.getEndDate());
        destination.setStartDate(mCalendar.getTime());

        mCalendar.add(Calendar.DAY_OF_MONTH, destination.getDuration());
        destination.setEndDate(mCalendar.getTime());
        mTrip.setEndDate(mCalendar.getTime());

        destinations.add(destination);

        mView.onDestinationAdded(destination);
        mView.onTripEndDateChanged(mTrip.getEndDate());

        /*//todo remove this
        SavedPlace savedPlace = new SavedPlace();
        savedPlace.setPlaceId(googlePlace.getId());
        savedPlace.setLongitude(googlePlace.getLatLng().longitude);
        savedPlace.setLatitude(googlePlace.getLatLng().latitude);
        savedPlace.setDestinationId(destination.getDestinationId());
        savedPlace.setName(googlePlace.getName().toString());
        savedPlaces.add(savedPlace);*/
    }

    @Override
    public void changeDestinationPlace(String oldDestinationId, Place googlePlace) {
        if (mView == null || !mView.isActive()) return;

        Destination newDestination = convertPlaceToDestination(googlePlace);
        for (int i = 0; i < mTrip.getDestinations().size(); i++) {
            Destination oldDest = mTrip.getDestinations().get(i);
            if (oldDestinationId.equals(oldDest.getDestinationId())) {
                newDestination.setDuration(oldDest.getDuration());
                newDestination.setOrder(oldDest.getOrder());
                mTrip.getDestinations().set(i, newDestination);
                break;
            }
        }
        mView.onDestinationChanged(oldDestinationId, newDestination);
    }

    @Override
    public void removeDestination(String oldDestinationId) {
        if (mView == null || !mView.isActive()) return;

        for (int i = 0; i < mTrip.getDestinations().size(); i++) {
            Destination oldDest = mTrip.getDestinations().get(i);
            if (oldDestinationId.equals(oldDest.getDestinationId())) {
                mTrip.getDestinations().remove(i);
                break;
            }
        }
        updateTripDates();

        mView.onDestinationRemoved(oldDestinationId);
        mView.onTripEndDateChanged(mTrip.getEndDate());
    }

    @Override
    public void changeTripStartDate(Date startDate) {
        if (startDate == null) return;
        if (mView == null || !mView.isActive()) return;

        mTrip.setStartDate(startDate);
        updateTripDates();

        mView.onTripEndDateChanged(mTrip.getEndDate());
    }

    @Override
    public void saveTrip(String name, Date startDate, List<String> destinationOrder) {
        if (mView == null || !mView.isActive()) {
            return;
        }
        if (name == null || "".equals(name.trim())) {
            mView.onTripSaveErrorEmptyName();
            return;
        }
        mTrip.setName(name.trim());
        mTrip.setStartDate(startDate);

        if (mTrip.getDestinations() == null || mTrip.getDestinations().size() == 0) {
            mView.onTripSaveErrorEmptyDestination();
            return;
        }

        updateDestinationsOrder(destinationOrder);
        updateTripDates();

        mRepository.updateTrip(mTrip, new DataSource.SaveTripCallback() {
            @Override
            public void onSuccess() {
                if (mView != null && mView.isActive()) {
                    mView.onTripSaved(mTrip);
                }
            }

            @Override
            public void onFailed() {
                if (mView != null && mView.isActive()) {
                    mView.onTripSaveFailure();
                }
            }
        });

        /*for (final SavedPlace savedPlace: savedPlaces) {
            mRepository.createSavedPlace(savedPlace, new DataSource.CreateSavedPlaceCallback() {
                @Override
                public void onSuccess() {
                    Log.d(LOG_TAG, "save place: "+savedPlace.getName());
                }

                @Override
                public void onFailed() {

                }
            });
        }*/
    }

    @Override
    public void createTripFromPlace(Place googlePlace) {
        if (mView == null || !mView.isActive()) {
            return;
        }
        Trip trip = new Trip();
        trip.setTripId(UUID.randomUUID().toString());

        List<Destination> destinations = new ArrayList<>();

        Destination destination = convertPlaceToDestination(googlePlace);
        destination.setDuration(DEFAULT_DURATION_IN_DAYS);
        destination.setTripId(trip.getTripId());
        destinations.add(destination);

        trip.setStartDate(new Date());
        mCalendar.setTime(trip.getStartDate());
        mCalendar.add(Calendar.DAY_OF_MONTH, DEFAULT_DURATION_IN_DAYS - 1);
        trip.setEndDate(mCalendar.getTime());

        trip.setDestinations(destinations);
        trip.setName(mView.getContext().getString(R.string.trip_addedit_default_trip_name, googlePlace.getName()));

        Log.d(LOG_TAG, "Created new trip: " + trip.getName() + ", end date: " + trip.getEndDate());

        mTrip = trip;
        mView.setTripDetails(trip);
    }

    private boolean isNewTrip() {
        return mTripId == null || "".equals(mTripId);
    }

    private Destination convertPlaceToDestination(Place googlePlace) {
        Destination destination = new Destination();
        destination.setDestinationId(UUID.randomUUID().toString());

        destination.setName(googlePlace.getName().toString());
        destination.setPlaceId(googlePlace.getId());

        destination.setLatitude(googlePlace.getLatLng().latitude);
        destination.setLongitude(googlePlace.getLatLng().longitude);

        return destination;
    }

    private void updateDestinationsOrder(List<String> destinationOrder) {
        if (destinationOrder == null || destinationOrder.size() == 0) return;
        if (mTrip.getDestinations() == null || mTrip.getDestinations().size() == 0) return;
        if (destinationOrder.size() != mTrip.getDestinations().size()) {
            Log.e(LOG_TAG, "Destinations order are not synchronized! ");
            return;
        }
        List<Destination> orderedList = new ArrayList<>();
        for (int i = 0; i < destinationOrder.size(); i++) {
            for (Destination destination : mTrip.getDestinations()) {
                if (destinationOrder.get(i).equals(destination.getDestinationId())) {
                    destination.setOrder(i);
                    orderedList.add(destination);
                    break;
                }
            }
        }
        mTrip.setDestinations(orderedList);
    }

    private void updateTripDates() {

        mCalendar.setTime(mTrip.getStartDate());

        int totalDuration = 0;
        for (Destination destination : mTrip.getDestinations()) {
            destination.setStartDate(mCalendar.getTime());
            mCalendar.add(Calendar.DAY_OF_MONTH, Math.max(0, destination.getDuration()));
            destination.setEndDate(mCalendar.getTime());
            totalDuration += destination.getDuration();
        }
        mCalendar.setTime(mTrip.getStartDate());
        mCalendar.add(Calendar.DAY_OF_MONTH, Math.max(0, totalDuration - 1));
        mTrip.setEndDate(mCalendar.getTime());
    }
}
