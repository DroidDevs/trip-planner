package droiddevs.com.tripplanner.adapters.triplist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;

/**
 * Created by Jared12 on 4/9/17.
 */

public class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public interface TripViewHolderListener {
        void OnTripClicked(int position);
    }

    @BindView(R.id.tvTripTitle) TextView tvTripTitle;
    @BindView(R.id.tvTripDates) TextView tvTripDate;
    @BindView(R.id.ivTripImage) ImageView ivTripImage;
    @BindView(R.id.ibTripMenu) ImageButton ibTripMenu;

    private TripViewHolderListener mListener;

    public TripViewHolder(View itemView, TripViewHolderListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            if (mListener != null) {
                mListener.OnTripClicked(position);
            }
        }
    }
}
