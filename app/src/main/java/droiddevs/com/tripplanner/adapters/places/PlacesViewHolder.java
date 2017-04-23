package droiddevs.com.tripplanner.adapters.places;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.map.PlaceItem;

/**
 * Created by elmira on 4/16/17.
 */

public class PlacesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivPlaceImage)
    ImageView imageView;

    @BindView(R.id.tvPlaceTitle)
    TextView nameTextView;

    @BindView(R.id.rbPlaceRating)
    RatingBar ratingBar;

    @BindView(R.id.tbFavorite)
    ToggleButton favoriteButton;

    @BindView(R.id.cardView)
    View cardView;

    private boolean settingSavedState = false;

    public PlacesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final PlaceItem placeItem,
                     final boolean savedPlace,
                     final PlacesAdapter.OnPlaceFavoriteCheckedListener favouriteCheckedListener,
                     final PlacesAdapter.OnPlaceClickedListener placeClickedListener) {

        nameTextView.setText(placeItem.getName());
        ratingBar.setRating(placeItem.getRating());

        String photoUrl = placeItem.getPhotoUrl();
        if (photoUrl != null) {
            Glide.with(imageView.getContext())
                    .load(photoUrl)
                    .centerCrop()
                    .into(imageView);
        }

        if (favouriteCheckedListener != null) {
            favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!settingSavedState) {
                        favouriteCheckedListener.onPlaceFavoriteChecked(placeItem, isChecked);
                    }
                    settingSavedState = false;
                }
            });
        }

        // setChecked fires the callback, which will delete places when scrolling, only if value changing
        settingSavedState = (favoriteButton.isChecked() != savedPlace);
        favoriteButton.setChecked(savedPlace);

        if (placeClickedListener != null) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placeClickedListener.onPlaceClicked(placeItem);
                }
            });
        }
    }
}
