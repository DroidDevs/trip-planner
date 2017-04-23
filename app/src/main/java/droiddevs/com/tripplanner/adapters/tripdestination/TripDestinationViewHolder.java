package droiddevs.com.tripplanner.adapters.tripdestination;

import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.PlaceOption;
import droiddevs.com.tripplanner.util.BitmapUtils;

/**
 * Created by Jared12 on 4/13/17.
 */

public class TripDestinationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public interface TripDestinationViewHolderListener {
        void OnOptionClicked(int position);
    }

    @BindView(R.id.ivDestinationOptionImage)
    ImageView ivDestinationOptionImage;

    @BindView(R.id.tvDestinationOptionTitle)
    TextView tvDesinationOptionTitle;

    @BindView(R.id.vPalette)
    View vPalette;

    private TripDestinationViewHolderListener mListener;

    public TripDestinationViewHolder(View itemView, TripDestinationViewHolderListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        itemView.setOnClickListener(this);
    }

    public void bind(PlaceOption option) {
        ivDestinationOptionImage.setImageDrawable(option.getOptionImageDrawable());
        tvDesinationOptionTitle.setText(option.getOptionTitle());

        Palette palette = Palette.from(BitmapUtils.getBitmapFromDrawable(vPalette.getContext(), option.getOptionImageDrawable())).generate();
        Palette.Swatch swatch = palette.getVibrantSwatch();
        if (swatch == null) swatch = palette.getDominantSwatch();

        if (swatch != null) {
            vPalette.setBackgroundColor(swatch.getRgb());
            tvDesinationOptionTitle.setTextColor(swatch.getTitleTextColor());
        }
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
