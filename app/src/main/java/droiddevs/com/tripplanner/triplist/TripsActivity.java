package droiddevs.com.tripplanner.triplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.Profile;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.addedittrip.AddEditTripActivity;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.login.OauthActivity;

import static droiddevs.com.tripplanner.addedittrip.AddEditTripFragment.ARGUMENT_TRIP_ID;

public class TripsActivity extends OauthActivity implements TripsFragment.TripFragmentCallbackListener {
    public static final String ARG_PAST_EVENTS = "past_events";
    private static final String LOG_DATA = "TripsActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nvView)
    NavigationView nvDrawer;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    TripsPresenter mTripsPresenter;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_DATA, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        ButterKnife.bind(this);

        // Setup toolbar
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.trips_title));
        setupFab();

        boolean pastEvents = getIntent().getBooleanExtra(ARG_PAST_EVENTS, false);
        if (!pastEvents) {
            mDrawerToggle = setupDrawerToggle();
            mDrawerLayout.addDrawerListener(mDrawerToggle);
            setupDrawerHeader(nvDrawer.getHeaderView(0));
            setupDrawerContent(nvDrawer);
        }
        else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getString(R.string.past_trips));
        }
        // Add fragment to content frame
        TripsFragment tripsFragment = (TripsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tripsFragment == null) {
            tripsFragment = TripsFragment.newInstance();
        }

        tripsFragment.setTripFragmentCallbackListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFrame, tripsFragment)
                .commit();


        // Create the presenter
        mTripsPresenter = new TripsPresenter(TripPlannerApplication.getRepository(), tripsFragment, pastEvents);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        return toggle;
    }

    @Override
    protected void onResume() {
        Log.d(LOG_DATA, "onResume()");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_DATA, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_DATA, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null
                && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        mDrawerLayout.closeDrawers(); // Close drawer
        switch (menuItem.getItemId()) {
            case R.id.nav_create_trip:
                showAddEditTrip(null); // Create trip
                break;
            case R.id.nav_view_past_trips:
                showPastTrips();
                break;
        }
    }

    private void showAddEditTrip(@Nullable String tripId) {
        // Create trip
        Intent intent = new Intent(this, AddEditTripActivity.class);
        if (tripId != null) {
            // Editing trip
            intent.putExtra(ARGUMENT_TRIP_ID, tripId);
        }
        startActivity(intent);
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void OnTripEditRequest(String tripId) {
        showAddEditTrip(tripId);
    }

    private void setupDrawerHeader(View headerLayout) {
        ParseUser currentParseUser = ParseUser.getCurrentUser();
        Profile currentUser = Profile.getCurrentProfile();
        if (currentUser != null) {
            TextView tvUsername = (TextView) headerLayout.findViewById(R.id.tvUsername);
            TextView tvUserEmail = (TextView) headerLayout.findViewById(R.id.tvUserEmail);
            ImageView ivUserImage = (ImageView) headerLayout.findViewById(R.id.ivUserImage);

            tvUsername.setText(currentUser.getName());

            if (currentParseUser != null) {
                String email = currentParseUser.getEmail();
                if (email != null && email.length() > 0) {
                    tvUserEmail.setText(email);
                }
            }

            Glide.with(TripsActivity.this)
                    .load(currentUser.getProfilePictureUri(400, 0))
                    .into(ivUserImage);
        }
    }

    private void showPastTrips() {
        Intent intent = new Intent(this, TripsActivity.class);
        intent.putExtra(ARG_PAST_EVENTS, true);
        startActivity(intent);
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    private void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEditTrip(null);
            }
        });
    }
}
