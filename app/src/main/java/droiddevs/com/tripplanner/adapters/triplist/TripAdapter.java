package droiddevs.com.tripplanner.adapters.triplist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Trip;

/**
 * Created by Jared12 on 4/9/17.
 */

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> implements TripViewHolder.TripViewHolderListener {
    public interface TripInteractionListener {
        void OnTripClicked(Trip trip);

        void OnTripMenuClicked(Trip trip, View anchorView);
    }

    private List<Trip> mTrips;
    private Context mContext;
    private TripInteractionListener mTripInteractionListener;

    public TripAdapter(Context context) {
        mContext = context;
        mTrips = new ArrayList<>();
    }

    public void setOnTripClickListener(TripInteractionListener listener) {
        mTripInteractionListener = listener;
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
        final Trip trip = mTrips.get(position);
        holder.tvTripTitle.setText(trip.getName());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d", Locale.US);
        String dateString = simpleDateFormat.format(trip.getStartDate()) + " - " + simpleDateFormat.format(trip.getEndDate());
        holder.tvTripDate.setText(dateString);

        String photoUrl = trip.getPhotoUrl();
        if (photoUrl != null) {
            Glide.with(getContext())
                    .load(photoUrl)
                    .centerCrop()
                    .into(holder.ivTripImage);
        }

        holder.ibTripMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTripInteractionListener.OnTripMenuClicked(trip, v);
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
        if (mTripInteractionListener != null) {
            mTripInteractionListener.OnTripClicked(trip);
        }
    }

    public void setTrips(List<Trip> trips) {
        mTrips.clear();
        mTrips.addAll(new ArrayList<Trip>(trips));
        notifyDataSetChanged();
    }

    public void deleteTrip(String tripId) {
        if (mTrips == null) return;
        for (int i = 0; i < mTrips.size(); i++) {
            Trip trip = mTrips.get(i);
            if (trip.getTripId().equals(tripId)) {
                mTrips.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public void addTrip(Trip trip) {
        int tripIndex = mTrips.indexOf(trip);
        if (tripIndex == -1) {
            mTrips.add(0, trip);
            notifyItemInserted(0);
        }
    }

    public void reloadTrip(Trip trip) {
        int tripIndex = mTrips.indexOf(trip);
        if (tripIndex != -1) {
            mTrips.set(tripIndex, trip);
            notifyItemChanged(tripIndex);
        }
        else addTrip(trip);
    }

    private Context getContext() {
        return mContext;
    }
}
