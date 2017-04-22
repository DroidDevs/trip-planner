package droiddevs.com.tripplanner.savedplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.login.OauthActivity;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.tripdetails.TripDetailsActivity;

/**
 * Created by elmira on 4/16/17.
 */

public class SavedPlacesActivity extends OauthActivity implements SavedPlacesContract.View {

    @BindView(R.id.include_toolbar)
    Toolbar toolbar;

    private MenuItem mListMenuItem;
    private MenuItem mMapMenuItem;

    private SavedPlacesContract.Presenter mPresenter;
    private Fragment mCurrentFragment;

    public static final String ARGUMENT_TRIP_ID = "tripId";
    public static final String ARGUMENT_DESTINATION_ID = "destId";

    private static final String FRAGMENT_TAG_LIST = "saved_places_list";
    private static final String FRAGMENT_TAG_MAP = "saved_places_map";

    private String mTripId;
    private String mDestinationId;

    private boolean hasData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);
        ButterKnife.bind(this);

        setupToolbar();

        mTripId = getIntent().getStringExtra(ARGUMENT_TRIP_ID);
        mDestinationId = getIntent().getStringExtra(ARGUMENT_DESTINATION_ID);

        mPresenter = new SavedPlacesPresenter(TripPlannerApplication.getRepository(), this, mDestinationId);

        loadListFragment();
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
        if (item.getItemId() == android.R.id.home) {
            finish();

            Intent intent = new Intent(this, TripDetailsActivity.class);
            intent.putExtra(TripDetailsActivity.ARGUMENT_TRIP_ID, mTripId);
            intent.putExtra(TripDetailsActivity.ARGUMENT_DESTINATION_ID, mDestinationId);
            startActivity(intent);

            return true;
        }
        else if (item.getItemId() == R.id.actionList) {
            mListMenuItem.setVisible(false);
            mMapMenuItem.setVisible(true);
            loadListFragment();
            return true;
        }
        else if (item.getItemId() == R.id.actionMap) {
            mListMenuItem.setVisible(true);
            mMapMenuItem.setVisible(false);
            loadMapFragment();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(SavedPlacesContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onPlacesLoaded(List<PlaceItem> places) {
        hasData = places != null && places.size() > 0;

        if (mCurrentFragment instanceof SavedPlacesListFragment) {
            ((SavedPlacesListFragment) mCurrentFragment).setData(places);
            if (mListMenuItem != null) mListMenuItem.setVisible(false);
            if (mMapMenuItem != null) mMapMenuItem.setVisible(hasData);
        }
        else if (mCurrentFragment instanceof SavedPlacesMapFragment) {
            ((SavedPlacesMapFragment) mCurrentFragment).setMapData(places);
            if (mListMenuItem != null) mListMenuItem.setVisible(hasData);
            if (mMapMenuItem != null) mMapMenuItem.setVisible(false);
        }
    }

    @Override
    public void onFailure() {

    }

    private void loadListFragment() {
        SavedPlacesListFragment savedPlacesListFragment = (SavedPlacesListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_LIST);
        if (savedPlacesListFragment == null) {
            savedPlacesListFragment = SavedPlacesListFragment.newInstance();
        }
        savedPlacesListFragment.setActivityPresenter(mPresenter);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, savedPlacesListFragment, FRAGMENT_TAG_LIST).commit();
        mCurrentFragment = savedPlacesListFragment;
    }

    private void loadMapFragment() {
        SavedPlacesMapFragment savedPlacesMapFragment = (SavedPlacesMapFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_MAP);
        if (savedPlacesMapFragment == null) {
            savedPlacesMapFragment = SavedPlacesMapFragment.newInstance();
        }
        savedPlacesMapFragment.setPresenter(mPresenter);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, savedPlacesMapFragment, FRAGMENT_TAG_MAP).commit();
        mCurrentFragment = savedPlacesMapFragment;
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.saved_places));
    }
}
