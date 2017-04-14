package droiddevs.com.tripplanner.adapters.tripmap;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.adapters.map.BaseMapAdapter;
import droiddevs.com.tripplanner.model.map.TripDestinationMapItem;

/**
 * Created by elmira on 4/11/17.
 */

public class TripMapItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvName)
    TextView nameTextView;

    @BindView(R.id.ivDestination)
    ImageView imageView;

    @BindView(R.id.card_view)
    CardView cardView;

    @BindView(R.id.tvStartDate)
    TextView startDateTextView;

    @BindView(R.id.tvEndDate)
    TextView endDateTextView;

    @BindView(R.id.tvDuration)
    TextView durationTextView;

    public TripMapItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final TripDestinationMapItem destination, final int position, final BaseMapAdapter.OnMapItemClickListener listener) {
        nameTextView.setText(destination.getName());

        //startDateTextView.setText(destination.getStartDate());
        //endDateTextView.setText(destination.getEndDate());
        //durationTextView.setText(destination.getDuration());

        if (listener != null) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMapItemClick(destination, position);
                }
            });
        }
    }
}