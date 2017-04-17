package droiddevs.com.tripplanner.adapters.savedplacemap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.model.map.PlaceItem;

/**
 * Created by elmira on 4/17/17.
 */

public class SavedPlaceMapAdapter extends BaseMapAdapter<PlaceItem> {

    @Override
    public SavedPlaceMapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SavedPlaceMapViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_saved_place, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PlaceItem placeItem = getMapDataItem(position);
        ((SavedPlaceMapViewHolder) holder).bind(placeItem);
    }
}