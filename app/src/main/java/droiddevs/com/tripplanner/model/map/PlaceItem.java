package droiddevs.com.tripplanner.model.map;

import com.google.android.gms.maps.model.LatLng;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;

/**
 * Created by elmira on 4/17/17.
 */

public class PlaceItem extends BaseMapItem {

    private double rating;
    private boolean saved;

    public PlaceItem(SavedPlace place, int position) {
        super(new LatLng(place.getLatitude(), place.getLongitude()), place.getName(), position, place.getPhotoUrl());
        this.rating = place.getRating();
        this.saved = true;
        setIconResId(R.drawable.ic_star_color_accent);
        setSelectedIconResId(R.drawable.ic_favorite_big);
    }

    public PlaceItem(GooglePlace place, int position, String photoUrl, boolean saved) {
        super(new LatLng(place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng()), place.getName(), position, photoUrl);
        this.rating = place.getRating();
        this.saved = saved;
    }

    public double getRating() {
        return rating;
    }

    public boolean isSaved() {
        return saved;
    }
}
