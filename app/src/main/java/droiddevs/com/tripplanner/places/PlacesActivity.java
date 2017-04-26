package droiddevs.com.tripplanner.places;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.model.map.PlaceItem;

import static droiddevs.com.tripplanner.tripdestination.TripDestinationFragment.ARGUMENT_TRIP_ID;
import static droiddevs.com.tripplanner.tripdestination.TripDestinationFragment.ARG_DESTINATION_ID;
import static droiddevs.com.tripplanner.tripdestination.TripDestinationFragment.ARG_PLACE_TYPE_SEARCH_STRING;
import static droiddevs.com.tripplanner.tripdestination.TripDestinationFragment.ARG_PLACE_TYPE_TITLE;

public class PlacesActivity extends AppCompatActivity implements PlacesContract.View {

    private PlacesContract.Presenter mPresenter;
    private static String LOG_TAG = "PlacesActivity";

    private String mPlaceTypeSearchString;
    private String mDestinationId;
    private String mTripId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.emptyViewStub)
    ViewStub emptyViewStub;

    @BindView(R.id.contentFrame)
    FrameLayout frameLayout;

    @BindView(R.id.loadingLayout)
    View loadingLayout;

    @BindView(R.id.failureViewStub)
    ViewStub failureViewStub;

    private MenuItem mListMenuItem;
    private MenuItem mMapMenuItem;

    private boolean hasData = false;
    private Fragment mCurrentFragment;

    private static final String FRAGMENT_TAG_LIST = "places_list";
    private static final String FRAGMENT_TAG_MAP = "places_map";

    public PlacesActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()");

        setContentView(R.layout.activity_places);
        ButterKnife.bind(this);

        this.mPlaceTypeSearchString = getIntent().getStringExtra(ARG_PLACE_TYPE_SEARCH_STRING);
        this.mDestinationId = getIntent().getStringExtra(ARG_DESTINATION_ID);
        this.mTripId = getIntent().getStringExtra(ARGUMENT_TRIP_ID);
        Log.d(LOG_TAG, "mDestinationId: " + mDestinationId + ", mTripId: " + mTripId);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Change actionbar title
        String typeTitle = getIntent().getStringExtra(ARG_PLACE_TYPE_TITLE);
        setTitle(typeTitle);

        mPresenter = new PlacesPresenter(
                TripPlannerApplication.getRepository(),
                this,
                mPlaceTypeSearchString,
                mDestinationId);

        loadListFragment();
    }

    private void loadListFragment() {
        PlacesListFragment listFragment =
                (PlacesListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_LIST);

        if (listFragment == null) {
            listFragment = PlacesListFragment.newInstance();
        }
        listFragment.setPresenter(mPresenter);
        listFragment.setDestinationId(mDestinationId);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFrame, listFragment, FRAGMENT_TAG_LIST);
        transaction.commit();

        mCurrentFragment = listFragment;
    }

    private void loadMapFragment() {
        PlacesMapFragment mapFragment = (PlacesMapFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_MAP);

        if (mapFragment == null) {
            mapFragment = PlacesMapFragment.newInstance();
        }
        mapFragment.setPresenter(mPresenter);

        getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, mapFragment, FRAGMENT_TAG_MAP).commit();
        mCurrentFragment = mapFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_places, menu);
        mListMenuItem = menu.findItem(R.id.actionList);
        mMapMenuItem = menu.findItem(R.id.actionMap);

        mMapMenuItem.setVisible(hasData);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.actionList:
                mListMenuItem.setVisible(false);
                mMapMenuItem.setVisible(true);
                loadListFragment();
                return true;

            case R.id.actionMap:
                mListMenuItem.setVisible(true);
                mMapMenuItem.setVisible(false);
                loadMapFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSuggestedPlaces(List<PlaceItem> places, Set<String> savedPlaceIds) {
        hasData = places != null && places.size() > 0;
        Log.d(LOG_TAG, "showSuggestedPlaces() hasData: " + hasData);

        if (!hasData) {
            emptyViewStub.inflate();
        }
        frameLayout.setVisibility(hasData ? View.VISIBLE : View.GONE);

        if (mCurrentFragment instanceof PlacesListFragment) {
            if (hasData) {
                ((PlacesListFragment) mCurrentFragment).setData(places);
                ((PlacesListFragment) mCurrentFragment).setSavedPlacesData(savedPlaceIds);
            }
            if (mListMenuItem != null) mListMenuItem.setVisible(false);
            if (mMapMenuItem != null) mMapMenuItem.setVisible(hasData);
        }
        else if (mCurrentFragment instanceof PlacesMapFragment) {
            if (hasData) {
                ((PlacesMapFragment) mCurrentFragment).setMapData(places);
            }
            if (mListMenuItem != null) mListMenuItem.setVisible(hasData);
            if (mMapMenuItem != null) mMapMenuItem.setVisible(false);
        }

    }

    @Override
    public void onSavedPlaceDeleted(PlaceItem placeItem) {
        if (mCurrentFragment instanceof PlacesListFragment) {
            ((PlacesListFragment) mCurrentFragment).onSavedPlaceDeleted(placeItem);
        }
    }

    @Override
    public void onFailure() {
        failureViewStub.inflate();
    }

    @Override
    public void setLoadingLayout(boolean isLoading) {
        Log.d(LOG_TAG, "setLoadingLayout() " + isLoading);
        loadingLayout.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setPresenter(PlacesContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}