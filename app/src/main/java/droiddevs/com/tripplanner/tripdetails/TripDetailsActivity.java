package droiddevs.com.tripplanner.tripdetails;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
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
        loadImagePerTabPosition(1);
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
                loadImagePerTabPosition(position - 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadImagePerTabPosition(int position) {
        if (position < 0) return;
        if (mTrip.getDestinations() != null && position < mTrip.getDestinations().size()) {
            String photoUrl = mTrip.getDestinations().get(position).getPhotoUrl();
            if (photoUrl != null) {
                Glide.with(TripDetailsActivity.this)
                        .load(photoUrl)
                        //.centerCrop()
                        .into(toolbarImage);
            }
        }
    }
}