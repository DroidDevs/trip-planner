package droiddevs.com.tripplanner.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.model.map.BaseMapItem;

import static droiddevs.com.tripplanner.R.id.map;
import static droiddevs.com.tripplanner.viewutil.BitmapUtils.getBitmapFromDrawable;

/**
 * Created by elmira on 4/09/17.
 */

public abstract class BaseMapFragment extends Fragment implements OnMapReadyCallback, MapContract.View<BaseMapItem>, GoogleMap.OnMarkerClickListener,
        BaseMapAdapter.OnMapItemClickListener<BaseMapItem> {

    private static final String LOG_TAG = "BaseMapFragment";

    @BindView(map)
    MapView mMapView;

    @BindView(R.id.rvMapData)
    RecyclerView mRecyclerView;

    private GoogleMap mGoogleMap;
    private BaseMapAdapter<BaseMapItem> mAdapter;

    private HashMap<Integer, Marker> hashMarkers;
    private int currentMarkerPosition = -1;

    private boolean isMapMarkersCreated = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hashMarkers = new HashMap<>();
        isMapMarkersCreated = false;

        mAdapter = getMapAdapter();
        mAdapter.setListener(this);
        setupRecyclerView();

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
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView!=null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        // Hide the zoom controls, compass and map toolbar as the bottom panel will cover it.
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        mGoogleMap.setOnMarkerClickListener(this);
        createMapMarkers();
    }

    @Override
    public void setMapData(List<BaseMapItem> data) {
        mAdapter.setMapData(data);
        createMapMarkers();
    }

    private void setupRecyclerView() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int position = layoutManager.findFirstVisibleItemPosition();
                navigateToMapItemMarker(position);
            }
        });
    }

    private void createMapMarkers() {
        if (isMapMarkersCreated || mGoogleMap == null || mAdapter == null || mAdapter.getItemCount() == 0)
            return;
        Log.d(LOG_TAG, "createMapMarkers()");

        isMapMarkersCreated = true;
        resetMapMarkers();

        List<BaseMapItem> mapMarkers = mAdapter.getMapData();
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for (BaseMapItem mapItem : mapMarkers) {
            Marker marker = null;
            if (currentMarkerPosition == -1) {
                marker = addNewMarker(mapItem.getSelectedIconResId() == 0 ? mAdapter.selectedIconResId : mapItem.getSelectedIconResId(), mapItem);
                currentMarkerPosition = 0;
            }
            else {
                marker = addNewMarker(mapItem.getIconResId(), mapItem);
            }
            hashMarkers.put(mapItem.getPosition(), marker);
            boundsBuilder.include(marker.getPosition());
        }

        LatLngBounds bounds = boundsBuilder.build();
        Log.d(LOG_TAG, "bounds.getCenter(): " + bounds.getCenter());

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height , padding);
        mGoogleMap.animateCamera(cu);
    }

    private void resetMapMarkers() {
        currentMarkerPosition = -1;
        mGoogleMap.clear();
        hashMarkers.clear();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        BaseMapItem mapItem = (BaseMapItem) marker.getTag();
        mRecyclerView.smoothScrollToPosition(mapItem.getPosition());
        return true;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    public abstract BaseMapAdapter getMapAdapter();

    @Override
    public void onMapItemClick(BaseMapItem data, int position) {
        //navigateToMapItemMarker(position);
    }

    private void navigateToMapItemMarker(int newPosition) {
        if (newPosition != currentMarkerPosition && hashMarkers.containsKey(newPosition)) {
            Log.d(LOG_TAG, "navigateToMapItemMarker: new position: " + newPosition + ", old position: " + currentMarkerPosition);

            Marker marker = hashMarkers.get(newPosition);
            BaseMapItem mapItem = (BaseMapItem) marker.getTag();
            if (mapItem == null) {
                Log.e(LOG_TAG, "marker tag is empty per position: " + newPosition);
                return;
            }
            marker.remove();
            marker = addNewMarker(mapItem.getSelectedIconResId() == 0 ? mAdapter.selectedIconResId : mapItem.getSelectedIconResId(), mapItem);
            marker.setZIndex(1);

            hashMarkers.put(newPosition, marker);

            if (hashMarkers.containsKey(currentMarkerPosition)) {
                Marker oldMarker = hashMarkers.get(currentMarkerPosition);
                mapItem = (BaseMapItem) oldMarker.getTag();
                if (mapItem != null) {
                    oldMarker.remove();
                    oldMarker = addNewMarker(mapItem.getIconResId(), mapItem);
                    oldMarker.setZIndex(0);

                    hashMarkers.put(currentMarkerPosition, oldMarker);
                }
                else {
                    Log.e(LOG_TAG, "marker tag is empty per position: " + currentMarkerPosition);
                }
            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), mGoogleMap.getCameraPosition().zoom);
            mGoogleMap.animateCamera(cameraUpdate);

            currentMarkerPosition = newPosition;
        }
    }

    private Marker addNewMarker(int iconResId, BaseMapItem mapItem) {
        Log.d(LOG_TAG, "add new marker: iconResId: " + iconResId + ", name: " + mapItem.getName());
        if (iconResId == 0) {
            iconResId = R.drawable.ic_place;
        }

        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                .position(mapItem.getLatLng())
                .title(mapItem.getName())
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(BaseMapFragment.this.getContext(), iconResId))));
        marker.setTag(mapItem);
        return marker;
    }
}