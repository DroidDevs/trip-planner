package droiddevs.com.tripplanner.adapters.tripmap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.PatternItem;

import java.util.Arrays;
import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.model.map.TripDestinationMapItem;

/**
 * Created by elmira on 4/11/17.
 */

public class TripMapAdapter extends BaseMapAdapter<TripDestinationMapItem> {

    private static final int PATTERN_GAP_LENGTH_PX = 12;
    private static final Dot DOT = new Dot();
    private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_DOTTED = Arrays.asList(DOT, GAP);

    private static final int STROKE_WIDTH_PX = 14;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TripMapItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_trip, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        TripDestinationMapItem item = getMapDataItem(position);
        ((TripMapItemViewHolder) holder).bind(item);
    }

    @Override
    public boolean isDrawPolylines() {
        return true;
    }

    @Override
    public List<PatternItem> getPolylinePattern() {
        return PATTERN_DOTTED;
    }

    @Override
    public int getPolylineColor() {
        return R.color.colorControlHighlight;
    }

    @Override
    public int getPolylineWidth() {
        return STROKE_WIDTH_PX;
    }

    @Override
    public boolean isPolylineGeodesic() {
        return false;
    }
}