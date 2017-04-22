package droiddevs.com.tripplanner.tripdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.addedittrip.AddEditTripActivity;
import droiddevs.com.tripplanner.addedittrip.AddEditTripFragment;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;

public class TripDetailsActivity extends AppCompatActivity implements TripDetailsContract.View {

    private static final String LOG_TAG = "TripDetailsActivity";

    public static final String ARGUMENT_TRIP_ID = "tripId";
    public static final String ARGUMENT_DESTINATION_ID = "destId";

    private String mTripId;
    private String mDestinationId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ivToolbarImage)
    ImageView toolbarImage;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    private TripDetailsContract.Presenter mPresenter;
    private TripDetailsFragmentPagerAdapter mPagerAdapter;

    private Trip mTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");

        mTripId = getIntent().getStringExtra(ARGUMENT_TRIP_ID);
        if (mTripId == null) {
            finish();
        }

        mDestinationId = getIntent().getStringExtra(ARGUMENT_DESTINATION_ID);
        if (mDestinationId != null) {
            setTheme(R.style.NoStatusBarTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        ButterKnife.bind(this);

        // Setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupTabs();

        mPresenter = new TripDetailsPresenter(this, TripPlannerApplication.getRepository(), mTripId);
        mPresenter.start();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        //todo set loading indicator
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy()");
    }

    @Override
    public void onTripLoaded(Trip trip) {
        Log.d(LOG_TAG, "");
        mTrip = trip;
        mPagerAdapter.setTrip(trip);

        toolbar.setTitle(trip.getName());
        toolbarImage.setVisibility(View.GONE);

        if (mDestinationId != null && mTrip.getDestinations() != null) {
            for (int i = 0; i < mTrip.getDestinations().size(); i++) {
                if (mDestinationId.equals(mTrip.getDestinations().get(i).getDestinationId())) {
                    loadImagePerTabPosition(i + 1);
                    mViewPager.setCurrentItem(i + 1);
                    appBarLayout.setExpanded(false, true);
                    break;
                }
            }
        }
    }

    @Override
    public void onLoadingFailure() {
        finish();
    }

    @Override
    public boolean isActive() {
        return !isFinishing();
    }

    @Override
    public void setPresenter(TripDetailsContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trip_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.action_edit) {
            Intent editIntent = new Intent(this, AddEditTripActivity.class);
            editIntent.putExtra(AddEditTripFragment.ARGUMENT_TRIP_ID, mTripId);
            startActivity(editIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTabs() {
        mPagerAdapter = new TripDetailsFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    toolbarImage.setVisibility(View.GONE);
                    toolbar.setBackgroundColor(ContextCompat.getColor(TripDetailsActivity.this, R.color.colorPrimary));
                }
                else {
                    loadImagePerTabPosition(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void loadImagePerTabPosition(int position) {
        if (position == 0) return;

        toolbarImage.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(ContextCompat.getColor(TripDetailsActivity.this, android.R.color.transparent));

        if (mTrip.getDestinations() != null && position <= mTrip.getDestinations().size()) {
            Destination destination = mTrip.getDestinations().get(position - 1);
            mDestinationId = destination.getDestinationId();
            String photoUrl = destination.getPhotoUrl();
            if (photoUrl != null) {
                Glide.with(TripDetailsActivity.this)
                        .load(photoUrl)
                        .centerCrop()
                        .into(toolbarImage);
            }
        }
    }
}