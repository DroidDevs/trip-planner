package droiddevs.com.tripplanner.savedplaces;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.login.OauthActivity;
import droiddevs.com.tripplanner.model.map.PlaceItem;

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

    public static final String ARGUMENT_DESTINATION_ID = "destId";

    private static final String FRAGMENT_TAG_LIST = "saved_places_list";
    private static final String FRAGMENT_TAG_MAP = "saved_places_map";

    private boolean hasData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);
        ButterKnife.bind(this);

        setupToolbar();

        String destinationId = getIntent().getStringExtra(ARGUMENT_DESTINATION_ID);
        mPresenter = new SavedPlacesPresenter(TripPlannerApplication.getRepository(), this, destinationId);

        loadListFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mPresenter.reloadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_saved_places, menu);
        mListMenuItem = menu.findItem(R.id.actionList);
        mMapMenuItem = menu.findItem(R.id.actionMap);

        updateMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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
        updateMenuItems();

        if (mCurrentFragment instanceof SavedPlacesListFragment) {
            ((SavedPlacesListFragment) mCurrentFragment).setData(places);
        }
        else if (mCurrentFragment instanceof SavedPlacesMapFragment) {
            ((SavedPlacesMapFragment) mCurrentFragment).setMapData(places);
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
        savedPlacesMapFragment.setActivityPresenter(mPresenter);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, savedPlacesMapFragment, FRAGMENT_TAG_MAP).commit();
        mCurrentFragment = savedPlacesMapFragment;
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvTitle = (TextView) ButterKnife.findById(toolbar, R.id.toolbar_title);
        tvTitle.setText(getString(R.string.saved_places));
    }

    private void updateMenuItems() {
        if (!hasData) {
            if (mListMenuItem != null) mListMenuItem.setVisible(false);
            if (mMapMenuItem != null) mMapMenuItem.setVisible(false);
        }
    }
}
