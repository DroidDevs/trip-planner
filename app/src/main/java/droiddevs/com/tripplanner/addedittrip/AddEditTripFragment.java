package droiddevs.com.tripplanner.addedittrip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.addedittrip.AddEditTripAdapter;
import droiddevs.com.tripplanner.adapters.addedittrip.StartDateViewHolder;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.viewutil.ItemOffsetDecoration;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by elmira on 4/4/17.
 */

public class AddEditTripFragment extends Fragment implements Contract.View, AddEditTripAdapter.OnChangeDestinationListener,
        AddEditTripAdapter.OnAddDestinationListener, AddEditTripAdapter.OnDeleteDestinationListener, StartDateViewHolder.OnStartDateChangeListener {
    private static final String LOG_TAG = "AddEditTripFragment";

    public static final String ARGUMENT_TRIP_ID = "tripId";
    private final static int UPDATE_TRIP_PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private final static int CREATE_TRIP_PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;

    private Contract.Presenter mPresenter;

    @BindView(R.id.rvAddEditTrip)
    RecyclerView mRecyclerView;

    private AddEditTripAdapter mAdapter;
    private String lastDestinationId;

    private String tripId = null;
    private boolean newTrip = true;

    private OnFragmentDoneListener mFragmentDoneListener;

    public interface OnFragmentDoneListener {
        void onDoneEdit(String tripId);
        void onTripAdded(String tripId);
    }

    public AddEditTripFragment() {
        //required public empty constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentDoneListener) {
            mFragmentDoneListener = (OnFragmentDoneListener) context;
        }
    }

    public static AddEditTripFragment newInstance(String tripId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_TRIP_ID, tripId);

        AddEditTripFragment fragment = new AddEditTripFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_tip, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripId = getArguments().getString(ARGUMENT_TRIP_ID, null);
        Log.d(LOG_TAG, "onCreate() tripId: " + tripId);

        if (tripId == null) {
            showPlaceAutocompleteDialog(CREATE_TRIP_PLACE_AUTOCOMPLETE_REQUEST_CODE);
        }
        else {
            newTrip = false;
            mPresenter.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mPresenter.destroy();
    }

    @Override
    public void setTripDetails(Trip trip) {
        mAdapter.setTrip(trip);
        tripId = trip.getTripId();
    }

    @Override
    public void onTripLoadFailure() {
        Toast.makeText(this.getContext(), R.string.trip_addedit_load_failed, Toast.LENGTH_SHORT).show();
        if (mFragmentDoneListener != null) {
            mFragmentDoneListener.onDoneEdit(null);
        }
    }

    @Override
    public void onTripSaved(Trip trip) {
        Toast.makeText(this.getContext(), newTrip ? R.string.trip_addedit_save_success :
                R.string.trip_addedit_update_success, Toast.LENGTH_SHORT).show();

        if (mFragmentDoneListener != null) {
            if (newTrip) {
                mFragmentDoneListener.onTripAdded(trip.getTripId());
            } else {
                mFragmentDoneListener.onDoneEdit(trip.getTripId());
            }
        }
    }

    @Override
    public void onTripSaveFailure() {
        Toast.makeText(this.getContext(), R.string.trip_addedit_save_failed, Toast.LENGTH_SHORT).show();
        if (mFragmentDoneListener != null) {
            mFragmentDoneListener.onDoneEdit(null);
        }
    }

    @Override
    public void onTripSaveErrorEmptyName() {
        Toast.makeText(this.getContext(), R.string.trip_addedit_error_message_empty_name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTripSaveErrorEmptyDestination() {
        Toast.makeText(this.getContext(), R.string.trip_addedit_error_message_empty_destination, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_TRIP_PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(LOG_TAG, "Point: " + place.getName());
                Log.i(LOG_TAG, "Last destination id: " + lastDestinationId);
                if (lastDestinationId == null) {
                    mPresenter.addNewDestinationPlace(place);
                }
                else {
                    mPresenter.changeDestinationPlace(lastDestinationId, place);
                }
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i(LOG_TAG, status.getStatusMessage());
            }
            else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        else if (requestCode == CREATE_TRIP_PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(LOG_TAG, "Point: " + place.getName());
                mPresenter.createTripFromPlace(place);
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i(LOG_TAG, status.getStatusMessage());
            }
            else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void saveTrip() {
        //this method is called by parent activity
        Log.d(LOG_TAG, "saveTrip()");
        mPresenter.saveTrip(mAdapter.getName(), mAdapter.getStartDate(), mAdapter.getDestinationOrderList());
    }

    private void setupRecyclerView() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new AddEditTripAdapter();
        mAdapter.setDestinationAddListener(this);

        mAdapter.setDestinationChangeListener(this);
        mAdapter.setDestinationDeleteListener(this);

        mAdapter.setStartDateChangeListener(this);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation()));
        mRecyclerView.addItemDecoration(new ItemOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.margin_small)));
    }

    @Override
    public void onChangeDestination(String destId) {
        this.lastDestinationId = destId;
        showPlaceAutocompleteDialog(UPDATE_TRIP_PLACE_AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onAddDestination() {
        this.lastDestinationId = null;
        showPlaceAutocompleteDialog(UPDATE_TRIP_PLACE_AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onDeleteDestination(String destId) {
        mPresenter.removeDestination(destId);
    }

    @Override
    public void onDestinationAdded(Destination destination) {
        mAdapter.addDestination(destination);
    }

    @Override
    public void onDestinationRemoved(String oldDestId) {
        mAdapter.deleteDestination(oldDestId);
    }

    @Override
    public void onDestinationChanged(String oldDestId, Destination destination) {
        mAdapter.changeDestination(oldDestId, destination);
    }

    @Override
    public void onStartDateChanged(Date startDate) {
        mPresenter.changeTripStartDate(startDate);
    }

    @Override
    public void onTripEndDateChanged(Date endDate) {
        mAdapter.setEndDate(endDate);
    }

    private void showPlaceAutocompleteDialog(int requestCode) {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(this.getActivity());
            startActivityForResult(intent, requestCode);
        } catch (Throwable e) {
            Log.e(LOG_TAG, e.toString());
        }
    }
}