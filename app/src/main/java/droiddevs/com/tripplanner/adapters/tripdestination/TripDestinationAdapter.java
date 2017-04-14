package droiddevs.com.tripplanner.adapters.tripdestination;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.DestinationOption;

/**
 * Created by Jared12 on 4/13/17.
 */

public class TripDestinationAdapter extends RecyclerView.Adapter<TripDestinationViewHolder> implements TripDestinationViewHolder.TripDestinationViewHolderListener {
    public interface DestinationOptionClickedListener {
        void OnOptionClicked(DestinationOption option);
    }

    private Context mContext;
    private List<DestinationOption> mDestinationOptions;
    private DestinationOptionClickedListener mListener;

    public TripDestinationAdapter(Context context, List<DestinationOption> destinationOptions) {
        mDestinationOptions = destinationOptions;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public TripDestinationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_trip_destination_option, parent, false);

        return new TripDestinationViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(TripDestinationViewHolder holder, int position) {
        DestinationOption option = mDestinationOptions.get(position);
        holder.ivDestinationOptionImage.setImageDrawable(option.getOptionImageDrawable());
        holder.tvDesinationOptionTitle.setText(option.getOptionTitle());
    }

    @Override
    public int getItemCount() {
        return mDestinationOptions.size();
    }

    public void setOptions(List<DestinationOption> options) {
        mDestinationOptions.clear();
        mDestinationOptions.addAll(options);
    }

    @Override
    public void OnOptionClicked(int position) {
        DestinationOption option = mDestinationOptions.get(position);
        mListener.OnOptionClicked(option);
    }

    public void setOnDestinationOptionClickedListener(DestinationOptionClickedListener listener) {
        mListener = listener;
    }
}
