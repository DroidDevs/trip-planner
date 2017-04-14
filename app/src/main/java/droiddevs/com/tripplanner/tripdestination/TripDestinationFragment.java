package droiddevs.com.tripplanner.tripdestination;

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
import droiddevs.com.tripplanner.model.DestinationOption;
import droiddevs.com.tripplanner.util.SpacesItemDecoration;

public class TripDestinationFragment extends Fragment implements TripDestinationContract.View, TripDestinationAdapter.DestinationOptionClickedListener {
    private static final String ARG_DESTINATION_ID = "destination_id";

    private List<DestinationOption> mDestinationOptions;
    private TripDestinationAdapter mAdapter;

    @BindView(R.id.rvDestinationOptions)
    RecyclerView rvDestinationOptions;
    private Unbinder unbinder;

    private String mDesinationId;
    private TripDestinationContract.Presenter mPresenter;

    public TripDestinationFragment() {}

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
            mDesinationId = getArguments().getString(ARG_DESTINATION_ID);
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
        mDestinationOptions = new ArrayList<>();
        mAdapter = new TripDestinationAdapter(getContext(), mDestinationOptions);
        mAdapter.setOnDestinationOptionClickedListener(this);

        rvDestinationOptions.setAdapter(mAdapter);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvDestinationOptions.setLayoutManager(gridLayoutManager);
        rvDestinationOptions.addItemDecoration(new SpacesItemDecoration(10));
    }

    @Override
    public void showDestinationOptions(List<DestinationOption> options) {
        mAdapter.setOptions(options);
    }

    @Override
    public void setPresenter(TripDestinationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void OnOptionClicked(DestinationOption option) {
        // TODO: LOAD OPTION ACTIVITY
    }
}
