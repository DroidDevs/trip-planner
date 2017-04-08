package droiddevs.com.tripplanner.adapters.addedittrip;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;

/**
 * Created by elmira on 4/6/17.
 */

public class AddViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvAddDestination)
    TextView addTextView;

    public AddViewHolder(View itemView, final AddEditTripAdapter.OnAddDestinationListener destinationAddListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        if (destinationAddListener!=null) {
            addTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    destinationAddListener.onAddDestination();
                }
            });
        }
    }
}