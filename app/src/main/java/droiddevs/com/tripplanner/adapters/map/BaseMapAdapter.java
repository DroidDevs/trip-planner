package droiddevs.com.tripplanner.adapters.map;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.map.BaseMapItem;

/**
 * Created by elmira on 4/11/17.
 */

public abstract class BaseMapAdapter<T extends BaseMapItem> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = "BaseMapAdapter";

    private List<T> mapData;

    public OnMapItemClickListener mListener;

    public int selectedIconResId;

    public interface OnMapItemClickListener<T> {
        void onMapItemClick(T data, int position);
    }

    public BaseMapAdapter() {
        this.selectedIconResId = R.drawable.ic_place_color_accent;
    }

    public void setMapData(List<T> data) {
        Log.d(LOG_TAG, "setMapData() data count: "+ (data==null? 0: data.size()));

        this.mapData = data;
        notifyDataSetChanged();
    }

    public List<T> getMapData() {
        return mapData;
    }

    public T getMapDataItem(int position) {
        return mapData.get(position);
    }

    @Override
    public int getItemCount() {
        return mapData == null ? 0 : mapData.size();
    }

    public void setListener(OnMapItemClickListener mListener) {
        this.mListener = mListener;
    }

    public int getSelectedIconResId() {
        return selectedIconResId;
    }

    public void setSelectedIconResId(int selectedIconResId) {
        this.selectedIconResId = selectedIconResId;
    }
}
