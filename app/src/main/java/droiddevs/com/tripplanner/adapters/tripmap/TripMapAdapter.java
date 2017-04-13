package droiddevs.com.tripplanner.adapters.tripmap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.model.map.TripDestinationMapItem;

/**
 * Created by elmira on 4/11/17.
 */

public class TripMapAdapter extends BaseMapAdapter<TripDestinationMapItem> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TripMapItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_trip, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TripDestinationMapItem item = getMapDataItem(position);
        ((TripMapItemViewHolder) holder).bind(item, position, mListener);
    }
}