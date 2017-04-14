package droiddevs.com.tripplanner.addedittrip;

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

public class AddEditTripActivity extends AppCompatActivity implements AddEditTripFragment.OnFragmentDoneListener {

    private static final String LOG_TAG = "AddEditTripActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Contract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_trip);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String tripId = getIntent().getStringExtra(AddEditTripFragment.ARGUMENT_TRIP_ID);

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
    public void onDoneEdit() {
        setResult(RESULT_OK);
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
}