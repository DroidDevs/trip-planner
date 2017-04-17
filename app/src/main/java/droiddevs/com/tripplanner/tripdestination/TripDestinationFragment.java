package droiddevs.com.tripplanner.tripdestination;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.tripdestination.TripDestinationAdapter;
import droiddevs.com.tripplanner.model.PlaceOption;
import droiddevs.com.tripplanner.savedplaces.SavedPlacesActivity;
import droiddevs.com.tripplanner.suggestedplaces.SuggestedPlacesActivity;
import droiddevs.com.tripplanner.util.SpacesItemDecoration;

public class TripDestinationFragment extends Fragment implements TripDestinationContract.View, TripDestinationAdapter.DestinationOptionClickedListener {
    public static final String ARG_DESTINATION_ID = "destination_id";
    public static final String ARG_PLACE_TYPE_SEARCH_STRING = "placeTypeSearchString";
    public static final String ARG_PLACE_TYPE_TITLE = "placeTypeTitle";

    private List<PlaceOption> mPlaceOptions;
    private TripDestinationAdapter mAdapter;

    @BindView(R.id.rvDestinationOptions)
    RecyclerView rvDestinationOptions;
    private Unbinder unbinder;

    private String mDestinationId;
    private TripDestinationContract.Presenter mPresenter;

    public TripDestinationFragment() {
    }

    public static TripDestinationFragment newInstance(String destinationId) {
        TripDestinationFragment fragment = new TripDestinationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DESTINATION_ID, destinationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDestinationId = getArguments().getString(ARG_DESTINATION_ID);
        }
        mPresenter = new TripDestinationPresenter(getContext(), this);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_destination, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPlaceOptions = new ArrayList<>();
        mAdapter = new TripDestinationAdapter(getContext(), mPlaceOptions);
        mAdapter.setOnDestinationOptionClickedListener(this);

        rvDestinationOptions.setAdapter(mAdapter);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvDestinationOptions.setLayoutManager(gridLayoutManager);
        rvDestinationOptions.addItemDecoration(new SpacesItemDecoration(10));
    }

    @Override
    public void showDestinationPlaceOptions(List<PlaceOption> options) {
        mAdapter.setOptions(options);
    }

    @Override
    public void setPresenter(TripDestinationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void OnOptionClicked(PlaceOption option) {
        if (option.getOptionType() == PlaceOption.PlaceOptionType.TYPE_SAVED_PLACES) {
            Intent intent = new Intent(getContext(), SavedPlacesActivity.class);
            intent.putExtra(SavedPlacesActivity.ARGUMENT_DESTINATION_ID, mDestinationId);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getContext(), SuggestedPlacesActivity.class);
            intent.putExtra(ARG_DESTINATION_ID, mDestinationId);
            intent.putExtra(ARG_PLACE_TYPE_SEARCH_STRING, option.getOptionType().typeSearchString());
            intent.putExtra(ARG_PLACE_TYPE_TITLE, option.getOptionTitle());
            startActivity(intent);
        }
    }
}
