package droiddevs.com.tripplanner.adapters.places;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.map.PlaceItem;

/**
 * Created by elmira on 4/16/17.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesViewHolder> {

    private List<PlaceItem> places;

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
        PlaceItem placeItem = places.get(position);
        holder.bind(placeItem, placeFavoriteCheckedListener, placeClickedListener);
    }

    public void setPlaces(List<PlaceItem> places) {
        this.places = new ArrayList<>(places);
        notifyDataSetChanged();
    }

    public void deletePlace(PlaceItem placeItem) {
        int index = this.places.indexOf(placeItem);
        if (index > -1) {
            this.places.remove(index);
            notifyItemRemoved(index);
        }
    }

    @Override
    public int getItemCount() {
        return places == null ? 0 : places.size();
    }

    public void setPlaceFavoriteCheckedListener(OnPlaceFavoriteCheckedListener placeFavoriteCheckedListener) {
        this.placeFavoriteCheckedListener = placeFavoriteCheckedListener;
    }

    public void setPlaceClickedListener(OnPlaceClickedListener placeClickedListener) {
        this.placeClickedListener = placeClickedListener;
    }
}