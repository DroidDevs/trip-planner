package droiddevs.com.tripplanner.placedetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.util.BitmapUtils;

import static droiddevs.com.tripplanner.R.id.map;

/**
 * Updated by Elmira Andreeva on 4/24/2017
 * Created by Jared12 on 4/15/17.
 */

public class PlaceDetailsFragment extends Fragment implements PlaceDetailsContract.View,
        OnMapReadyCallback {

    private static final String LOG_TAG = "PlaceDetailsFragment";

    @BindView(R.id.tvPlaceTitle)
    TextView tvPlaceTitle;

    @BindView(R.id.rbPlaceRating)
    RatingBar rbPlaceRating;

    @BindView(R.id.tvRating)
    TextView ratingTextView;

    @BindView(R.id.tvOpenNow)
    TextView tvOpenNow;

    @BindView(map)
    MapView mMapView;

    @BindView(R.id.failureViewStub)
    ViewStub failureViewStub;

    @BindView(R.id.tvDirections)
    TextView tvDirections;

    @BindView(R.id.tvCall)
    TextView tvCall;

    @BindView(R.id.tvShare)
    TextView tvShare;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;

    private Unbinder mUnbinder;
    private PlaceDetailsContract.Presenter mPresenter;

    private boolean isMapReady;
    private GoogleMap mGoogleMap;

    public PlaceDetailsFragment() {
    }

    public static PlaceDetailsFragment newInstance() {
        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isMapReady = false;
        mGoogleMap = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_place_details, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void setPresenter(PlaceDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPlaceDetails(final PlaceItem place) {

        tvPlaceTitle.setText(place.getName());
        rbPlaceRating.setRating(place.getRating());
        ratingTextView.setText("" + place.getRating());

        List<Object> weekdayHours = place.getWeekdayHours();
        if (weekdayHours != null && weekdayHours.size() > 0) {
            tvOpenNow.setText(weekdayHours.get(0).toString());
        }
        else {
            tvOpenNow.setVisibility(View.INVISIBLE);
        }

        tvDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:mode=d&q=" + place.getLatLng().latitude + "," + place.getLatLng().longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, place.getName() + ". Address: " + place.getFormattedAddress());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        tvAddress.setText(place.getFormattedAddress());

        if (place.getPhoneNumber() != null && !"".equals(place.getPhoneNumber())) {
            tvPhoneNumber.setText(place.getPhoneNumber());

            tvCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + place.getPhoneNumber()));
                    startActivity(intent);
                }
            });
        }
        else {
            tvPhoneNumber.setVisibility(View.GONE);
        }
        addMarkerToMap();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(LOG_TAG, "onMapReady()");

        mGoogleMap = map;
        isMapReady = true;

        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

        addMarkerToMap();
    }

    private void addMarkerToMap() {
        PlaceItem place = mPresenter.getCurrentPlace();
        if (place == null || !isMapReady) return;

        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions()
                .position(place.getLatLng())
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getBitmapFromDrawable(PlaceDetailsFragment.this.getContext(), R.drawable.ic_map_place_details))));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onFailure() {
        failureViewStub.inflate();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
