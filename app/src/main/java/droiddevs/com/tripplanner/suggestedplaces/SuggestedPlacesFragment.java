package droiddevs.com.tripplanner.suggestedplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.suggestedplaces.SuggestedPlacesAdapter;
import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.suggestedplacedetails.SuggestedPlaceDetailsActivity;

public class SuggestedPlacesFragment extends Fragment implements SuggestedPlacesContract.View, SuggestedPlacesAdapter.SuggestedPlaceInteractionListener {

    private List<GooglePlace> mPlaces;
    private SuggestedPlacesContract.Presenter mPresenter;
    private SuggestedPlacesAdapter mAdapter;
    private Unbinder unbinder;

    @BindView(R.id.rvSuggestedPlaces)
    RecyclerView rvSuggestedPlaces;

    public static SuggestedPlacesFragment newInstance() {
        return new SuggestedPlacesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggested_places, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPlaces = new ArrayList<>();
        mAdapter = new SuggestedPlacesAdapter(getContext(), mPlaces);
        mAdapter.setSuggestedPlaceInteractionListener(this);

        rvSuggestedPlaces.setAdapter(mAdapter);
        rvSuggestedPlaces.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvSuggestedPlaces.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showSuggestedPlaces(List<GooglePlace> places) {
        mAdapter.setPlaces(places);
    }

    @Override
    public void setPresenter(SuggestedPlacesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void OnPlaceClicked(GooglePlace place) {
        Intent intent = new Intent(getContext(), SuggestedPlaceDetailsActivity.class);
        startActivity(intent);
    }
}
