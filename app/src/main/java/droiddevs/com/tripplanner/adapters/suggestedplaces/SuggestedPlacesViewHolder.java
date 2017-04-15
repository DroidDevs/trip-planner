package droiddevs.com.tripplanner.adapters.suggestedplaces;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;

/**
 * Created by Jared12 on 4/15/17.
 */

public class SuggestedPlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public interface SuggestedPlacesViewHolderListener {
        void OnPlaceClicked(int position);
    }

    private SuggestedPlacesViewHolderListener mListener;

    @BindView(R.id.tvPlaceTitle)
    TextView tvPlaceTitle;

    public SuggestedPlacesViewHolder(View itemView, SuggestedPlacesViewHolderListener listener) {
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
                mListener.OnPlaceClicked(position);
            }
        }
    }
}
