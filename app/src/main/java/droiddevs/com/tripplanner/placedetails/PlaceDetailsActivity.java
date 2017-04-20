package droiddevs.com.tripplanner.placedetails;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.model.googleplaces.Photo;

public class PlaceDetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivToolbarImage)
    ImageView toolbarImage;

    private String mDestinationId;

    //todo this should be a PlaceItem object
    private GooglePlace mCurrentPlace;
    private PlaceDetailsContract.Presenter mPresenter;

    public static final String ARG_PLACE_OBJ = "placeObj";
    public static final String ARG_DESTINATION_ID = "destinationId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_place_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        //todo this should be a PlaceItem object
        mCurrentPlace = getIntent().getParcelableExtra(ARG_PLACE_OBJ);

        mDestinationId = getIntent().getStringExtra(ARG_DESTINATION_ID);
        loadPlaceImage();

        // Add fragment to content frame
        PlaceDetailsFragment suggestedPlaceDetailsFragment =
                (PlaceDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (suggestedPlaceDetailsFragment == null) {
            suggestedPlaceDetailsFragment = PlaceDetailsFragment.newInstance();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentFrame, suggestedPlaceDetailsFragment);
            transaction.commit();
        }

        mPresenter = new PlaceDetailsPresenter(
                TripPlannerApplication.getRepository(),
                suggestedPlaceDetailsFragment,
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

        List<Photo> placePhotos = mCurrentPlace.getPhotos();
        if (placePhotos.size() > 0) {
            Photo placePhoto = placePhotos.get(0);
            Glide.with(this)
                    .load(placePhoto.getFullPhotoURLReference())
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
}
