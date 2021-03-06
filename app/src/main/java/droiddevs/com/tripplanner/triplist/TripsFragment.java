package droiddevs.com.tripplanner.triplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.triplist.TripAdapter;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.tripdetails.TripDetailsActivity;

/**
 * Created by jared.manfredi on 4/5/17.
 * Updated by Elmira Andreeva on 4/20/17
 */

public class TripsFragment extends Fragment implements TripsContract.View, TripAdapter.TripInteractionListener {
    public interface TripFragmentCallbackListener {
        void OnTripEditRequest(String tripId);
    }

    private TripFragmentCallbackListener mListener;

    private TripsContract.Presenter mPresenter;
    private TripAdapter mAdapter;
    private Unbinder unbinder;
    private boolean mPastEvents = false;

    @BindView(R.id.rvTrips)
    RecyclerView rvTrips;

    @BindView(R.id.loadingLayout)
    View loadingLayout;

    @BindView(R.id.emptyViewStub)
    ViewStub emptyViewStub;

    @BindView(R.id.failureViewStub)
    ViewStub failureViewStub;

    public static TripsFragment newInstance() {
        TripsFragment fragment = new TripsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setPresenter(TripsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new TripAdapter(getContext());
        mAdapter.setOnTripClickListener(this);

        rvTrips.setAdapter(mAdapter);
        rvTrips.setHasFixedSize(true); // Improves scroll performance

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvTrips.setLayoutManager(linearLayoutManager);
        //rvTrips.setItemAnimator(new SlideInUpAnimator());
    }

    @Override
    public void showTrips(List<Trip> trips) {
        if (trips == null || trips.size() == 0) {
            emptyViewStub.inflate();
        }
        else {
            mAdapter.setTrips(trips);
            rvTrips.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onTripDeleted(String tripId) {
        mAdapter.deleteTrip(tripId);
        if (mAdapter.getItemCount()==0){
            emptyViewStub.inflate();
        }
    }

    @Override
    public void OnTripClicked(Trip trip) {
        Intent detailsIntent = new Intent(getContext(), TripDetailsActivity.class);
        detailsIntent.putExtra(TripDetailsActivity.ARGUMENT_TRIP_ID, trip.getTripId());
        startActivity(detailsIntent);
    }

    @Override
    public void OnTripMenuClicked(Trip trip, View anchorView) {
        showTripMenuPopup(trip, anchorView);
    }

    private void showTripMenuPopup(final Trip trip, View anchorView) {
        ActionBar actionBar = ((TripsActivity) getActivity()).getSupportActionBar();
        PopupMenu popup = new PopupMenu(actionBar.getThemedContext(), anchorView);
        popup.getMenuInflater().inflate(R.menu.popup_trip_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        mPresenter.deleteTrip(trip);
                        return true;
                    case R.id.menu_edit:
                        if (mListener != null) {
                            mListener.OnTripEditRequest(trip.getTripId());
                            return true;
                        }
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    public void setTripFragmentCallbackListener(TripFragmentCallbackListener listener) {
        mListener = listener;
    }

    public void setPastEvents(boolean pastEvents) {
        this.mPastEvents = pastEvents;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setLoadingLayout(boolean isLoading) {
        loadingLayout.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFailure() {
        failureViewStub.inflate();
    }
}
