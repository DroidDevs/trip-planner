package droiddevs.com.tripplanner.adapters.tripdestination;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;

/**
 * Created by Jared12 on 4/13/17.
 */

public class TripDestinationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public interface TripDestinationViewHolderListener {
        void OnOptionClicked(int position);
    }

    @BindView(R.id.ivDestinationOptionImage) ImageView ivDestinationOptionImage;
    @BindView(R.id.tvDestinationOptionTitle) TextView tvDesinationOptionTitle;

    private TripDestinationViewHolderListener mListener;

    public TripDestinationViewHolder(View itemView, TripDestinationViewHolderListener listener) {
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
                mListener.OnOptionClicked(position);
            }
        }
    }
}
