package droiddevs.com.tripplanner.adapters.placemap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.model.map.PlaceItem;

/**
 * Created by elmira on 4/17/17.
 */

public class PlaceMapAdapter extends BaseMapAdapter<PlaceItem> {

    @Override
    public PlaceMapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlaceMapViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_place, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        PlaceItem placeItem = getMapDataItem(position);
        ((PlaceMapViewHolder) holder).bind(placeItem);
    }
}