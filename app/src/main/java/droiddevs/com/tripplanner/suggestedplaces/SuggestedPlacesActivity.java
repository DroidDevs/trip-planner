package droiddevs.com.tripplanner.suggestedplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.tripdetails.TripDetailsActivity;

import static droiddevs.com.tripplanner.tripdestination.TripDestinationFragment.ARGUMENT_TRIP_ID;
import static droiddevs.com.tripplanner.tripdestination.TripDestinationFragment.ARG_DESTINATION_ID;
import static droiddevs.com.tripplanner.tripdestination.TripDestinationFragment.ARG_PLACE_TYPE_SEARCH_STRING;
import static droiddevs.com.tripplanner.tripdestination.TripDestinationFragment.ARG_PLACE_TYPE_TITLE;

public class SuggestedPlacesActivity extends AppCompatActivity implements SuggestedPlacesContract.View {

    private SuggestedPlacesContract.Presenter mPresenter;

    private String mPlaceTypeSearchString;
    private String mDestinationId;
    private String mTripId;

    @BindView(R.id.include_toolbar)
    Toolbar toolbar;

    private MenuItem mListMenuItem;
    private MenuItem mMapMenuItem;

    private boolean hasData = false;
    private Fragment mCurrentFragment;

    private static final String FRAGMENT_TAG_LIST = "places_list";
    private static final String FRAGMENT_TAG_MAP = "places_map";

    public SuggestedPlacesActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_places);
        ButterKnife.bind(this);

        this.mPlaceTypeSearchString = getIntent().getStringExtra(ARG_PLACE_TYPE_SEARCH_STRING);
        this.mDestinationId = getIntent().getStringExtra(ARG_DESTINATION_ID);
        this.mTripId = getIntent().getStringExtra(ARGUMENT_TRIP_ID);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Change actionbar title
        String typeTitle = getIntent().getStringExtra(ARG_PLACE_TYPE_TITLE);
        getSupportActionBar().setTitle(typeTitle);

        mPresenter = new SuggestedPlacesPresenter(
                TripPlannerApplication.getRepository(),
                this,
                mPlaceTypeSearchString,
                mDestinationId);

        loadListFragment();
    }

    private void loadListFragment() {
        SuggestedPlacesListFragment listFragment =
                (SuggestedPlacesListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_LIST);

        if (listFragment == null) {
            listFragment = SuggestedPlacesListFragment.newInstance();
        }
        listFragment.setPresenter(mPresenter);
        listFragment.setDestinationId(mDestinationId);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFrame, listFragment, FRAGMENT_TAG_LIST);
        transaction.commit();

        mCurrentFragment = listFragment;
    }

    private void loadMapFragment() {
        SuggestedPlacesMapFragment mapFragment = (SuggestedPlacesMapFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_MAP);
        if (mapFragment == null) {
            mapFragment = SuggestedPlacesMapFragment.newInstance();
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
                finish();

                Intent intent = new Intent(this, TripDetailsActivity.class);
                intent.putExtra(TripDetailsActivity.ARGUMENT_TRIP_ID, mTripId);
                intent.putExtra(TripDetailsActivity.ARGUMENT_DESTINATION_ID, mDestinationId);
                startActivity(intent);

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

        if (mCurrentFragment instanceof SuggestedPlacesListFragment) {
            ((SuggestedPlacesListFragment) mCurrentFragment).setData(places);
            ((SuggestedPlacesListFragment) mCurrentFragment).setSavedPlacesData(savedPlaceIds);
            if (mListMenuItem != null) mListMenuItem.setVisible(false);
            if (mMapMenuItem != null) mMapMenuItem.setVisible(hasData);
        }
        else if (mCurrentFragment instanceof SuggestedPlacesMapFragment) {
            ((SuggestedPlacesMapFragment) mCurrentFragment).setMapData(places);
            if (mListMenuItem != null) mListMenuItem.setVisible(hasData);
            if (mMapMenuItem != null) mMapMenuItem.setVisible(false);
        }
    }

    @Override
    public void onSavedPlaceDeleted(PlaceItem placeItem) {
        if (mCurrentFragment instanceof SuggestedPlacesListFragment) {
            ((SuggestedPlacesListFragment) mCurrentFragment).onSavedPlaceDeleted(placeItem);
        }
    }

    @Override
    public void setPresenter(SuggestedPlacesContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}