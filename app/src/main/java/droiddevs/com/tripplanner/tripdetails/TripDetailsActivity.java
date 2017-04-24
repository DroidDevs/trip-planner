package droiddevs.com.tripplanner.tripdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

    @BindView(R.id.loadingLayout)
    View loadingLayout;

    @BindView(R.id.failureViewStub)
    ViewStub failureViewStub;

    private TripDetailsContract.Presenter mPresenter;
    private TripDetailsFragmentPagerAdapter mPagerAdapter;

    private Trip mTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        ButterKnife.bind(this);

        mTripId = getIntent().getStringExtra(ARGUMENT_TRIP_ID);
        if (mTripId == null && savedInstanceState != null) {
            mTripId = savedInstanceState.getString(ARGUMENT_TRIP_ID);
        }
        mDestinationId = getIntent().getStringExtra(ARGUMENT_DESTINATION_ID);
        if (mDestinationId == null && savedInstanceState != null) {
            mDestinationId = savedInstanceState.getString(ARGUMENT_DESTINATION_ID);
        }
        Log.d(LOG_TAG, "mTripId: " + mTripId + ", mDestinationId: " + mDestinationId);
        if (mTripId == null) {
            finish();
        }
        // Setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mDestinationId == null) {
            appBarLayout.setExpanded(false);
        }

        setupTabs();

        mPresenter = new TripDetailsPresenter(this, TripPlannerApplication.getRepository(), mTripId);
        mPresenter.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);

        outState.putString(ARGUMENT_TRIP_ID, mTripId);

        if (mDestinationId != null) {
            outState.putString(ARGUMENT_DESTINATION_ID, mDestinationId);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void setLoadingIndicator(boolean isLoading) {
        loadingLayout.setVisibility(isLoading ? View.VISIBLE : View.GONE);
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
        Log.d(LOG_TAG, "onTripLoaded() " + trip.toString() + ", mDestinationId: " + mDestinationId);
        mTrip = trip;
        mPagerAdapter.setTrip(trip);

        toolbarImage.setVisibility(View.GONE);

        setupToolbarTitle();
        allowEachTabWithEqualWidth();

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
        failureViewStub.inflate();
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
        mPagerAdapter = new TripDetailsFragmentPagerAdapter(getSupportFragmentManager(), getBaseContext());
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
                    appBarLayout.setExpanded(false, true);
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

    private void allowEachTabWithEqualWidth() {
        ViewGroup slidingTabStrip = (ViewGroup) mTabLayout.getChildAt(0);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            View tab = slidingTabStrip.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab.getLayoutParams();
            layoutParams.weight = 1;
            tab.setLayoutParams(layoutParams);
        }
    }

    private void loadImagePerTabPosition(int position) {
        if (position == 0) return;

        toolbarImage.setVisibility(View.VISIBLE);

        if (mTrip.getDestinations() != null && position <= mTrip.getDestinations().size()) {
            Destination destination = mTrip.getDestinations().get(position - 1);
            mDestinationId = destination.getDestinationId();
            String photoUrl = destination.getPhotoUrl();
            if (photoUrl != null) {
                Glide.with(TripDetailsActivity.this)
                        .load(photoUrl)
                        .centerCrop()
                        .into(toolbarImage);

                appBarLayout.setExpanded(true, true);
            }
        }
    }

    private void setupToolbarTitle() {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(mTrip.getName());

        ssb.append("\n");
        int start = ssb.length();

        String dateRange = "";
        ssb.append(dateRange);
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(this, R.style.TextSmall);
        ssb.setSpan(textAppearanceSpan, start, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        collapsingToolbarLayout.setTitle(ssb);
    }
}