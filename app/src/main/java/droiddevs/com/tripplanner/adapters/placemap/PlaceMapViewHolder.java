package droiddevs.com.tripplanner.adapters.placemap;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.map.PlaceItem;

/**
 * Created by elmira on 4/17/17.
 */

public class PlaceMapViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivPlaceView)
    ImageView imageView;

    @BindView(R.id.tvName)
    TextView nameTextView;

    @BindView(R.id.tvRating)
    TextView ratingTextView;

    @BindView(R.id.rbPlaceRating)
    RatingBar ratingBar;

    public PlaceMapViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(PlaceItem placeItem) {
        nameTextView.setText(placeItem.getName());

        if (placeItem.getRating() > 0) {
            ratingTextView.setText("" + placeItem.getRating());
            ratingBar.setRating(placeItem.getRating());
        }

        String photoUrl = placeItem.getPhotoUrl();
        if (photoUrl != null) {
            Glide.with(imageView.getContext())
                    .load(photoUrl)
                    .centerCrop()
                    .into(imageView);
        }
    }
}
