package droiddevs.com.tripplanner.adapters.places;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.map.PlaceItem;

/**
 * Created by elmira on 4/16/17.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesViewHolder> {

    private List<PlaceItem> mPlaces;
    private Set<String> mSavedPlaceIds;

    private OnPlaceFavoriteCheckedListener placeFavoriteCheckedListener;
    private OnPlaceClickedListener placeClickedListener;

    public interface OnPlaceFavoriteCheckedListener {
        void onPlaceFavoriteChecked(PlaceItem placeItem, boolean checked);
    }

    public interface OnPlaceClickedListener {
        void onPlaceClicked(PlaceItem placeItem);
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlacesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false));
    }

    @Override
    public void onBindViewHolder(PlacesViewHolder holder, int position) {

        PlaceItem placeItem = mPlaces.get(position);
        holder.bind(placeItem,
                mSavedPlaceIds.contains(placeItem.getPlaceId()),
                placeFavoriteCheckedListener,
                placeClickedListener);
    }

    public void setPlaces(List<PlaceItem> places) {
        this.mPlaces = new ArrayList<>(places);
        notifyDataSetChanged();
    }

    public void deletePlace(PlaceItem placeItem) {
        int index = this.mPlaces.indexOf(placeItem);
        if (index > -1) {
            this.mPlaces.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void setPlaceSaved(boolean save, String placeId) {
        if (save) {
            mSavedPlaceIds.add(placeId);
        } else {
            mSavedPlaceIds.remove(placeId);
        }
    }

    @Override
    public int getItemCount() {
        return mPlaces == null ? 0 : mPlaces.size();
    }

    public void setPlaceFavoriteCheckedListener(OnPlaceFavoriteCheckedListener placeFavoriteCheckedListener) {
        this.placeFavoriteCheckedListener = placeFavoriteCheckedListener;
    }

    public void setPlaceClickedListener(OnPlaceClickedListener placeClickedListener) {
        this.placeClickedListener = placeClickedListener;
    }

    public void setPlacesSavedIdSet(Set<String> idSet) {
        this.mSavedPlaceIds = idSet;
    }
}