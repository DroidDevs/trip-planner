package droiddevs.com.tripplanner.triplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.Profile;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.addedittrip.AddEditTripActivity;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.login.LoginActivity;

import static droiddevs.com.tripplanner.addedittrip.AddEditTripFragment.ARGUMENT_TRIP_ID;

public class TripsActivity extends AppCompatActivity implements TripsFragment.TripFragmentCallbackListener {
    public static final int ADD_EDIT_TRIP_REQUEST = 1;

    public static final int ADD_TRIP_RESULT_TYPE = 2;
    public static final int EDIT_TRIP_RESULT_TYPE = 3;

    public static final String ARGUMENT_TRIP_RESULT_TYPE = "result_type";

    @BindView(R.id.include_toolbar) Toolbar toolbar;
    @BindView(R.id.nvView) NavigationView nvDrawer;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;

    TripsPresenter mTripsPresenter;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        ButterKnife.bind(this);

        // Setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerHeader(nvDrawer.getHeaderView(0));
        setupDrawerContent(nvDrawer);

        View headerLayout = nvDrawer.getHeaderView(0);
        TextView tvUsername = (TextView) headerLayout.findViewById(R.id.tvUsername);
        Profile currentUser = Profile.getCurrentProfile();
        tvUsername.setText(currentUser.getName());

        // Change toolbar title
        TextView tvTitle = (TextView)
                ButterKnife.findById(toolbar, R.id.toolbar_title);
        tvTitle.setText("Trips");

        // Check for user authentication
        if (AccessToken.getCurrentAccessToken() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        // Add fragment to content frame
        if (savedInstanceState == null) {
            TripsFragment tripsFragment =
                    (TripsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
            if (tripsFragment == null) {
                tripsFragment = TripsFragment.newInstance();
                tripsFragment.setTripFragmentCallbackListener(this);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.contentFrame, tripsFragment);
                transaction.commit();
            }

            // Create the presenter
            mTripsPresenter = new TripsPresenter(TripPlannerApplication.getRepository(), tripsFragment);
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        return toggle;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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
        switch (menuItem.getItemId()) {
            case R.id.nav_create_trip:
                showAddEditTrip(null); // Create trip
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_EDIT_TRIP_REQUEST) {
            if (resultCode == RESULT_OK) {
                String tripId = data.getStringExtra(ARGUMENT_TRIP_ID);
                int resultType = data.getIntExtra(ARGUMENT_TRIP_RESULT_TYPE, 0);
                switch (resultType) {
                    case ADD_TRIP_RESULT_TYPE:
                        mTripsPresenter.addTrip(tripId);
                        break;
                    case EDIT_TRIP_RESULT_TYPE:
                        mTripsPresenter.reloadTripAfterEdit(tripId);
                        break;
                }
            }
        }
    }

    private void showAddEditTrip(@Nullable String tripId) {
        // Create trip
        Intent intent = new Intent(this, AddEditTripActivity.class);
        if (tripId != null) {
            // Editing trip
            intent.putExtra(ARGUMENT_TRIP_ID, tripId);
        }
        startActivityForResult(intent, ADD_EDIT_TRIP_REQUEST);
    }

    @Override
    public void OnTripEditRequest(String tripId) {
        showAddEditTrip(tripId);
    }

    private void setupDrawerHeader(View headerLayout) {
        TextView tvUsername = (TextView) headerLayout.findViewById(R.id.tvUsername);
        TextView tvUserEmail = (TextView) headerLayout.findViewById(R.id.tvUserEmail);
        ImageView ivUserImage = (ImageView) headerLayout.findViewById(R.id.ivUserImage);

        Profile currentUser = Profile.getCurrentProfile();
        tvUsername.setText(currentUser.getName());

        Glide.with(TripsActivity.this)
                .load(currentUser.getProfilePictureUri(100, 0))
                .into(ivUserImage);
    }
}