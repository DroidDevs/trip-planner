package droiddevs.com.tripplanner.model.map;

import com.google.android.gms.maps.model.LatLng;

import droiddevs.com.tripplanner.R;

/**
 * Created by elmira on 4/11/17.
 */

public class BaseMapItem {

    private LatLng latLng;
    private String name;
    private int position;

    private int iconResId;
    private int selectedIconResId;
    private String photoUrl;

    public BaseMapItem(LatLng latLng, String name, int position, String photoUrl) {
        this.latLng = latLng;
        this.name = name;
        this.position = position;
        this.iconResId = R.drawable.ic_place_color_primary;
        this.selectedIconResId = R.drawable.ic_place_color_accent;
        this.photoUrl = photoUrl;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getPosition() {
        return position;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getName() {
        return name;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSelectedIconResId() {
        return selectedIconResId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
