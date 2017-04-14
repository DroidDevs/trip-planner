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
import android.widget.TextView;

import com.facebook.AccessToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.addedittrip.AddEditTripActivity;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.login.LoginActivity;

import static java.lang.Thread.sleep;

public class TripsActivity extends AppCompatActivity {
    static final int ADD_EDIT_TRIP_REQUEST = 1;

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
        setupDrawerContent(nvDrawer);

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
        TripsFragment tripsFragment =
                (TripsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tripsFragment == null) {
            tripsFragment = TripsFragment.newInstance();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, tripsFragment);
            transaction.commit();
        }

        // Create the presenter
        mTripsPresenter = new TripsPresenter(TripPlannerApplication.getRepository(), tripsFragment);
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
                Intent intent = new Intent(this, AddEditTripActivity.class);
                startActivityForResult(intent, ADD_EDIT_TRIP_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_EDIT_TRIP_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Trip Added
                // TODO: THIS PROBALBY SHOULDNT RELOAD THE ENTIRE LIST
                // FIXME: Why does this need a delay to reload?
                // We need to send the created trip back and load directly anyways
                try {
                    sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mTripsPresenter.start();
            }
        }
    }
}