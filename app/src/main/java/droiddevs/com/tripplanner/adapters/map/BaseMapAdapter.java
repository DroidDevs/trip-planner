package droiddevs.com.tripplanner.adapters.map;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.PatternItem;

import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.map.BaseMapItem;

/**
 * Created by elmira on 4/11/17.
 */

public abstract class BaseMapAdapter<T extends BaseMapItem> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = "BaseMapAdapter";

    private List<T> mapData;

    private OnMapItemClickListener<T> mOnClickListener;

    public int selectedIconResId;

    public interface OnMapItemClickListener<T> {
        void onMapItemClick(T data);
    }

    public BaseMapAdapter() {
        this.selectedIconResId = R.drawable.ic_place_color_accent;
    }

    public void setMapData(List<T> data) {
        Log.d(LOG_TAG, "setMapData() data count: " + (data == null ? 0 : data.size()));

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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onMapItemClick(getMapData().get(position));
                }
            });
        }
    }

    public void setOnClickListener(OnMapItemClickListener<T> mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public int getSelectedIconResId() {
        return selectedIconResId;
    }

    public void setSelectedIconResId(int selectedIconResId) {
        this.selectedIconResId = selectedIconResId;
    }

    public boolean isDrawPolylines() {
        return false;
    }

    public List<PatternItem> getPolylinePattern() {
        return null;
    }

    public int getPolylineColor() {
        return 0;
    }

    public int getPolylineWidth() {
        return 0;
    }

    public boolean isPolylineGeodesic() {
        return false;
    }
}
