package droiddevs.com.tripplanner.adapters.addedittrip;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Destination;

/**
 * Created by elmira on 4/6/17.
 */

public class DestinationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvDestinationTitle)
    TextView titleTextView;

    @BindView(R.id.tvDuration)
    TextView durationTextView;

    @BindView(R.id.ivClose)
    ImageView closeImageView;

    View rootView;

    public DestinationViewHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Destination destination, final AddEditTripAdapter.OnChangeDestinationListener destinationChangeListener,
                     final AddEditTripAdapter.OnDeleteDestinationListener destinationDeleteListener,
                     final AddEditTripAdapter.OnSelectDurationListener onSelectDurationListener) {

        titleTextView.setText(destination.getName());
        durationTextView.setText(destination.getDuration() + " days");

        if (onSelectDurationListener != null) {
            durationTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectDurationListener.onSelectDuration(destination.getDestinationId());
                }
            });
        }
        if (destinationChangeListener != null) {
            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    destinationChangeListener.onChangeDestination(destination.getDestinationId());
                }
            });
        }
        if (destinationDeleteListener != null) {
            closeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    destinationDeleteListener.onDeleteDestination(destination.getDestinationId());
                }
            });
        }
    }

    public void onItemSelected(){
        rootView.setBackgroundColor(ContextCompat.getColor(rootView.getContext(), R.color.background_color_light));
    }

    public void onItemClear(){
        rootView.setBackgroundColor(ContextCompat.getColor(rootView.getContext(), android.R.color.transparent));
    }
}
