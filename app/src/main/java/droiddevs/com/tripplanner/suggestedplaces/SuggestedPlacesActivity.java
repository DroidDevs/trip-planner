package droiddevs.com.tripplanner.suggestedplaces;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.application.TripPlannerApplication;

import static droiddevs.com.tripplanner.tripdestination.TripDestinationFragment.ARG_DESTINATION_ID;
import static droiddevs.com.tripplanner.tripdestination.TripDestinationFragment.ARG_PLACE_TYPE_SEARCH_STRING;
import static droiddevs.com.tripplanner.tripdestination.TripDestinationFragment.ARG_PLACE_TYPE_TITLE;

public class SuggestedPlacesActivity extends AppCompatActivity {
    SuggestedPlacesContract.Presenter mPresenter;

    private String mPlaceTypeSearchString;
    private String mDestinationId;

    @BindView(R.id.include_toolbar)
    Toolbar toolbar;

    public SuggestedPlacesActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_places);
        ButterKnife.bind(this);

        setPlaceTypeSearchString(getIntent().getStringExtra(ARG_PLACE_TYPE_SEARCH_STRING));
        setDestinationId(getIntent().getStringExtra(ARG_DESTINATION_ID));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Change actionbar title
        String typeTitle = getIntent().getStringExtra(ARG_PLACE_TYPE_TITLE);
        setTitle(typeTitle);

        // Add fragment to content frame
        SuggestedPlacesFragment suggestedPlacesFragment =
                (SuggestedPlacesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (suggestedPlacesFragment == null) {
            suggestedPlacesFragment = SuggestedPlacesFragment.newInstance();
            suggestedPlacesFragment.setDestinationId(mDestinationId);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentFrame, suggestedPlacesFragment);
            transaction.commit();
        }

        mPresenter = new SuggestedPlacesPresenter(
                TripPlannerApplication.getRepository(),
                suggestedPlacesFragment,
                mPlaceTypeSearchString,
                mDestinationId);
    }

    public void setPlaceTypeSearchString(String placeTypeSearchString) {
        this.mPlaceTypeSearchString = placeTypeSearchString;
    }

    public void setDestinationId(String destinationId) {
        this.mDestinationId = destinationId;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
