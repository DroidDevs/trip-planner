package droiddevs.com.tripplanner.addedittrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.application.TripPlannerApplication;

import static droiddevs.com.tripplanner.addedittrip.AddEditTripFragment.ARGUMENT_TRIP_ID;
import static droiddevs.com.tripplanner.triplist.TripsActivity.ADD_TRIP_RESULT_TYPE;
import static droiddevs.com.tripplanner.triplist.TripsActivity.ARG_TRIP_RESULT_TYPE;
import static droiddevs.com.tripplanner.triplist.TripsActivity.EDIT_TRIP_RESULT_TYPE;

public class AddEditTripActivity extends AppCompatActivity implements AddEditTripFragment.OnFragmentDoneListener {

    private static final String LOG_TAG = "AddEditTripActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Contract.Presenter mPresenter;
    private boolean isNewTrip = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_trip);
        ButterKnife.bind(this);

        setupToolbar();

        String tripId = getIntent().getStringExtra(ARGUMENT_TRIP_ID);
        isNewTrip = (tripId == null || "".equalsIgnoreCase(tripId));

        AddEditTripFragment addEditTripFragment = (AddEditTripFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (addEditTripFragment == null) {
            addEditTripFragment = AddEditTripFragment.newInstance(tripId);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, addEditTripFragment)
                    .commit();
        }

        mPresenter = new AddEditTripPresenter(TripPlannerApplication.getRepository(), addEditTripFragment, tripId);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEditTripFragment addEditTripFragment = (AddEditTripFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (addEditTripFragment != null) {
                    addEditTripFragment.saveTrip();
                }
            }
        });
    }

    @Override
    public void onDoneEdit(String tripId) {
        Intent resultIntent;
        if (tripId != null) {
            resultIntent = new Intent();
            resultIntent.putExtra(ARG_TRIP_RESULT_TYPE, EDIT_TRIP_RESULT_TYPE);
            resultIntent.putExtra(ARGUMENT_TRIP_ID, tripId);
            setResult(RESULT_OK, resultIntent);
        }
        else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void onTripAdded(String tripId) {
        Intent resultIntent;
        if (tripId != null) {
            resultIntent = new Intent();
            resultIntent.putExtra(ARG_TRIP_RESULT_TYPE, ADD_TRIP_RESULT_TYPE);
            resultIntent.putExtra(ARGUMENT_TRIP_ID, tripId);
            setResult(RESULT_OK, resultIntent);
        }
        else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(isNewTrip ? R.string.trip_create : R.string.trip_edit);
    }
}