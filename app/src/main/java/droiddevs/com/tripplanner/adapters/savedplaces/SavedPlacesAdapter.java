package droiddevs.com.tripplanner.adapters.savedplaces;

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

public class SavedPlacesAdapter extends RecyclerView.Adapter<SavedPlacesViewHolder> {

    private List<PlaceItem> places;

    @Override
    public SavedPlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SavedPlacesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_place, parent, false));
    }

    @Override
    public void onBindViewHolder(SavedPlacesViewHolder holder, int position) {
        PlaceItem placeItem = places.get(position);
        holder.bind(placeItem);
    }

    public void setPlaces(List<PlaceItem> places) {
        this.places = new ArrayList<>(places);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return places == null ? 0 : places.size();
    }
}