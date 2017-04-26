package droiddevs.com.tripplanner.places;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.places.PlacesAdapter;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.placedetails.PlaceDetailsActivity;

public class PlacesListFragment extends Fragment implements
        PlacesAdapter.OnPlaceClickedListener, PlacesAdapter.OnPlaceFavoriteCheckedListener {

    private PlacesContract.Presenter mPresenter;
    private PlacesAdapter mAdapter;
    private Unbinder unbinder;
    private String mDestinationId;
    private boolean mPlaceDetailsOpened;

    @BindView(R.id.rvSuggestedPlaces)
    RecyclerView rvSuggestedPlaces;

    public static PlacesListFragment newInstance() {
        return new PlacesListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // We dont want to reload from cache when returning from details
        // incase the saved state was changed, we need to reload.
        // Should probably return state from details activity and reload only cell if need be
        if (mPlaceDetailsOpened) {
            mPlaceDetailsOpened = false;
            mPresenter.start();
        }
        else {
            mPresenter.reloadData();
        }
    }

    public void setPresenter(PlacesContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new PlacesAdapter();
        mAdapter.setPlaceClickedListener(this);
        mAdapter.setPlaceFavoriteCheckedListener(this);

        rvSuggestedPlaces.setAdapter(mAdapter);
        rvSuggestedPlaces.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvSuggestedPlaces.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setData(List<PlaceItem> places) {
        mAdapter.setPlaces(places);
    }

    public void setSavedPlacesData(Set<String> savedPlacesIds) {
        mAdapter.setPlacesSavedIdSet(savedPlacesIds);
    }

    public void setDestinationId(String destinationId) {
        mDestinationId = destinationId;
    }

    @Override
    public void onPlaceClicked(PlaceItem placeItem) {
        mPlaceDetailsOpened = true;
        Intent intent = new Intent(getContext(), PlaceDetailsActivity.class);
        intent.putExtra(PlaceDetailsActivity.ARG_PLACE_OBJ, placeItem);
        intent.putExtra(PlaceDetailsActivity.ARG_DESTINATION_ID, mDestinationId);
        startActivity(intent);
    }

    @Override
    public void onPlaceFavoriteChecked(PlaceItem placeItem, boolean checked) {
        if (checked) {
            mPresenter.savePlace(placeItem);
        }
        else {
            mPresenter.deletePlace(placeItem);
        }
        mAdapter.setPlaceSaved(checked, placeItem.getPlaceId());
    }

    public void onSavedPlaceDeleted(PlaceItem placeItem) {
        mAdapter.deletePlace(placeItem);
    }
}
