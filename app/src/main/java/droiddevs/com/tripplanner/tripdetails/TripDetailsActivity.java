package droiddevs.com.tripplanner.tripdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import droiddevs.com.tripplanner.model.Trip;

public class TripDetailsActivity extends AppCompatActivity implements TripDetailsContract.View {

    public static final String ARGUMENT_TRIP_ID = "tripId";
    private String mTripId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ivToolbarImage)
    ImageView toolbarImage;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private TripDetailsContract.Presenter mPresenter;
    private TripDetailsFragmentPagerAdapter mPagerAdapter;

    private Trip mTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        ButterKnife.bind(this);

        mTripId = getIntent().getStringExtra(ARGUMENT_TRIP_ID);
        if (mTripId == null) {
            finish();
        }

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
    public void onTripLoaded(Trip trip) {
        mTrip = trip;
        mPagerAdapter.setTrip(trip);

        toolbar.setTitle(trip.getName());
        toolbarImage.setVisibility(View.GONE);
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
                    toolbarImage.setVisibility(View.VISIBLE);
                    toolbar.setBackgroundColor(ContextCompat.getColor(TripDetailsActivity.this, android.R.color.transparent));
                    loadImagePerTabPosition(position - 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void loadImagePerTabPosition(int position) {
        if (position < 0) return;
        if (mTrip.getDestinations() != null && position < mTrip.getDestinations().size()) {
            String photoUrl = mTrip.getDestinations().get(position).getPhotoUrl();
            if (photoUrl != null) {
                Glide.with(TripDetailsActivity.this)
                        .load(photoUrl)
                        .centerCrop()
                        .into(toolbarImage);
            }
        }
    }
}