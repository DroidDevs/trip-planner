package droiddevs.com.tripplanner.savedplaces;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.places.PlacesAdapter;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.viewutil.ItemOffsetDecoration;

/**
 * Created by elmira on 4/16/17.
 */

public class SavedPlacesListFragment extends Fragment implements PlacesAdapter.OnPlaceClickedListener, PlacesAdapter.OnPlaceFavoriteCheckedListener {

    @BindView(R.id.rvSavedPlaces)
    RecyclerView mRecyclerView;

    @BindView(R.id.emptyList)
    View emptyListLayout;

    private Unbinder unbinder;
    private PlacesAdapter mAdapter;

    private SavedPlacesContract.Presenter mPresenter;

    public static SavedPlacesListFragment newInstance() {
        return new SavedPlacesListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_saved_places_list, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.reloadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public void setActivityPresenter(SavedPlacesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setData(List<PlaceItem> placeItems) {
        if (isAdded() && mAdapter != null) {
            mAdapter.setPlaces(placeItems);
            boolean hasData = placeItems != null && placeItems.size() > 0;

            emptyListLayout.setVisibility(!hasData ? View.VISIBLE : View.GONE);
            mRecyclerView.setVisibility(hasData ? View.VISIBLE : View.GONE);
        }
    }

    private void setupRecyclerView() {
        mAdapter = new PlacesAdapter();
        mAdapter.setPlaceClickedListener(this);
        mAdapter.setPlaceFavoriteCheckedListener(this);

        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation()));
        mRecyclerView.addItemDecoration(new ItemOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.margin_small)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onPlaceClicked(PlaceItem placeItem) {
        /*Intent intent = new Intent(getContext(), PlaceDetailsActivity.class);
        intent.putExtra(PlaceDetailsActivity.ARG_PLACE_OBJ, place);
        intent.putExtra(PlaceDetailsActivity.ARG_DESTINATION_ID, mDestinationId);
        startActivity(intent);*/
    }

    @Override
    public void onPlaceFavoriteChecked(PlaceItem placeItem, boolean checked) {

    }
}