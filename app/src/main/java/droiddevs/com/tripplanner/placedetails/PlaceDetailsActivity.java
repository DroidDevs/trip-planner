package droiddevs.com.tripplanner.placedetails;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.widget.FABToggle;

/**
 * Updated by Elmira Andreeva on 4/24/2017
 * Created by Jared12 on 4/15/17.
 */

public class PlaceDetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ivToolbarImage)
    ImageView toolbarImage;

    @BindView(R.id.fabHeart)
    FABToggle fab;

    private String mDestinationId;

    private PlaceItem mCurrentPlace;
    private PlaceDetailsContract.Presenter mPresenter;

    public static final String ARG_PLACE_OBJ = "placeObj";
    public static final String ARG_DESTINATION_ID = "destinationId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        mCurrentPlace = getIntent().getParcelableExtra(ARG_PLACE_OBJ);

        mDestinationId = getIntent().getStringExtra(ARG_DESTINATION_ID);
        loadPlaceImage();

        // Add fragment to content frame
        PlaceDetailsFragment placeDetailsFragment =
                (PlaceDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (placeDetailsFragment == null) {
            placeDetailsFragment = PlaceDetailsFragment.newInstance();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFrame, placeDetailsFragment);
        transaction.commit();

        setupFab();

        mPresenter = new PlaceDetailsPresenter(
                TripPlannerApplication.getRepository(),
                placeDetailsFragment,
                mCurrentPlace,
                mDestinationId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void loadPlaceImage() {
        if (mCurrentPlace == null) return;

        String placeUrl = mCurrentPlace.getPhotoUrl();
        if (placeUrl != null) {
            Glide.with(this)
                    .load(placeUrl)
                    .centerCrop()
                    .into(toolbarImage);
        }
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

    private void setupFab() {

        if (mCurrentPlace != null) {
            fab.setChecked(mCurrentPlace.isSaved());
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.toggle();
                if (fab.isChecked()) {
                    mPresenter.savePlace();
                }
                else {
                    mPresenter.deletePlace();
                }
            }
        });
    }
}
