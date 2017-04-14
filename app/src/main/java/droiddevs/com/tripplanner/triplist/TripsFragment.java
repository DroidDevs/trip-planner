package droiddevs.com.tripplanner.triplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
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
 */

public class TripsFragment extends Fragment implements TripsContract.View, TripAdapter.TripInteractionListener {

    private List<Trip> mTrips;
    private TripsContract.Presenter mPresenter;
    private TripAdapter mAdapter;

    @BindView(R.id.rvTrips) RecyclerView rvTrips;
    private Unbinder unbinder;

    public static TripsFragment newInstance() {
        return new TripsFragment();
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

        mTrips = new ArrayList<>();
        mAdapter = new TripAdapter(getContext(), mTrips);
        mAdapter.setOnTripClickListener(this);

        rvTrips.setAdapter(mAdapter);
        rvTrips.setHasFixedSize(true); // Improves scroll performance

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvTrips.setLayoutManager(linearLayoutManager);
        //rvTrips.setItemAnimator(new SlideInUpAnimator());
    }

    @Override
    public void showTrips(List<Trip> trips) {
        mAdapter.setTrips(trips);
        rvTrips.scrollToPosition(0);
    }

    @Override
    public void onTripDeleted(int position) {
        mAdapter.deleteTrip(position);
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
        PopupMenu popup = new PopupMenu(anchorView.getContext(), anchorView);
        popup.getMenuInflater().inflate(R.menu.popup_trip_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        int position = mTrips.indexOf(trip);
                        mPresenter.deleteTrip(trip, position);
                        return true;
                    case R.id.menu_edit:
                        Toast.makeText(getActivity(), "Edit!", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }
}
