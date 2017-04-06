package droiddevs.com.tripplanner.addedittrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;

public class AddEditTripActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AddEditTripActivity";

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.cityView)
    TextView textView;

    Contract.Presenter mPresenter;

    //todo ONLY testing: remove these fields later
    private Trip trip;
    private Repository repository;
    private int order = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_trip);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        //mPresenter = new AddEditTripPresenter(getApplication());

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(LOG_TAG, "Point: " + place.getName());
                textView.setText(String.format("name: %s, id: %s, phone: %s, rating: %s, address: %s",
                        place.getName(),
                        place.getId(),
                        place.getPhoneNumber(),
                        place.getRating(),
                        place.getAddress()));

                addNewDestination(place);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(LOG_TAG, "An error occurred: " + status);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS).build();
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(AddEditTripActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (Throwable e) {
                    Log.e(LOG_TAG, e.toString());
                }*/
                updateTrip();
            }
        });

        // todo TESTING only
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 2);

        repository = TripPlannerApplication.getRepository();
        trip = new Trip();
        trip.setTripId(UUID.randomUUID().toString());

        trip.setStartDate(new Date());
        trip.setEndDate(calendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                textView.setText(String.format("name: %s, id: %s, phone: %s, rating: %s, address: %s",
                        place.getName(),
                        place.getId(),
                        place.getPhoneNumber(),
                        place.getRating(),
                        place.getAddress()));

                addNewDestination(place);
                Log.i(LOG_TAG, "Point: " + place.getName());
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(LOG_TAG, status.getStatusMessage());

            }
            else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void addNewDestination(Place place) {
        Destination destination = convertPlaceToDestination(place);

        destination.setStartDate(new Date());
        destination.setEndDate(new Date());

        destination.setOrder(order++);
        destination.setDuration(0);

        destination.setTripId(trip.getTripId());
        addDestinationToTrip(destination);
    }

    public void updateTrip() {
        generateTripName();
        repository.updateTrip(trip, new DataSource.SaveTripCallback() {
            @Override
            public void onSuccess() {
                loadTrip();
            }

            @Override
            public void onFailed() {

            }
        });
    }

    private Destination convertPlaceToDestination(Place googlePlace) {
        Destination destination = new Destination();
        destination.setName(googlePlace.getName().toString());
        destination.setPointId(googlePlace.getId());

        destination.setLatitude(googlePlace.getLatLng().latitude);
        destination.setLongitude(googlePlace.getLatLng().longitude);

        destination.setPhoneNumber(googlePlace.getPhoneNumber().toString());
        destination.setRating(googlePlace.getRating());

        destination.setTypes(googlePlace.getPlaceTypes());
        destination.setDestinationId(UUID.randomUUID().toString());

        return destination;
    }

    private void addDestinationToTrip(Destination destination) {
        List<Destination> list = trip.getDestinations();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(destination);
        trip.setDestinations(list);
    }

    private void loadTrip() {
        String uid = trip.getTripId();

        repository.loadTrip(uid, new DataSource.LoadTripCallback() {
            @Override
            public void onTripLoaded(Trip trip) {

                StringBuilder sb = new StringBuilder();
                sb.append(trip.toString());
                sb.append("\n");

                List<Destination> destinations = trip.getDestinations();
                for (Destination dest : destinations) {
                    sb.append(dest.toString());
                    sb.append("\n");
                }
                textView.setText(sb.toString());
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void generateTripName() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tour to ");
        for (Destination dest : trip.getDestinations()) {
            sb.append(dest.getName());
            sb.append(" ");
        }
        trip.setName(sb.toString());
    }
}