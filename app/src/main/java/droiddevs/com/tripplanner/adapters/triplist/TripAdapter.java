package droiddevs.com.tripplanner.adapters.triplist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Trip;

/**
 * Created by Jared12 on 4/9/17.
 */

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> implements TripViewHolder.TripViewHolderListener {
    public interface TripClickedListener {
        void OnTripClicked(Trip trip);
    }

    private List<Trip> mTrips;
    private Context mContext;
    private TripClickedListener mTripClickListener;

    public TripAdapter(Context context, List<Trip> trips) {
        mContext = context;
        mTrips = trips;
    }

    private Context getContext() {
        return mContext;
    }

    public void setOnTripClickListener(TripClickedListener listener) {
        mTripClickListener = listener;
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemTrip = layoutInflater.inflate(R.layout.item_trip, parent, false);

        return new TripViewHolder(itemTrip, this);
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        Trip trip = mTrips.get(position);
        holder.tvTripTitle.setText(trip.getName());
        //holder.ivTripImage.setImageBitmap();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d", Locale.US);
        String dateString = simpleDateFormat.format(trip.getStartDate()) + " - " + simpleDateFormat.format(trip.getEndDate());
        holder.tvTripDate.setText(dateString);

        holder.ibTripMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: SHOW MENU OPTIONS TO DELETE TRIP
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    @Override
    public void OnTripClicked(int position) {
        Trip trip = mTrips.get(position);
        if (mTripClickListener != null) {
            mTripClickListener.OnTripClicked(trip);
        }
    }

    public void setTrips(List<Trip> trips) {
        mTrips.clear();
        mTrips.addAll(trips);
        notifyDataSetChanged();
    }

    private void removeTrip(int position) {
        mTrips.remove(position);
        notifyItemRemoved(position);
    }
}
