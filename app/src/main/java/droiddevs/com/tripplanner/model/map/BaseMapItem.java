package droiddevs.com.tripplanner.model.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import droiddevs.com.tripplanner.R;

/**
 * Created by elmira on 4/11/17.
 */

public class BaseMapItem implements Parcelable {

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

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public void setSelectedIconResId(int selectedIconResId) {
        this.selectedIconResId = selectedIconResId;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.latLng, flags);
        dest.writeString(this.name);
        dest.writeInt(this.position);
        dest.writeInt(this.iconResId);
        dest.writeInt(this.selectedIconResId);
        dest.writeString(this.photoUrl);
    }

    protected BaseMapItem(Parcel in) {
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.name = in.readString();
        this.position = in.readInt();
        this.iconResId = in.readInt();
        this.selectedIconResId = in.readInt();
        this.photoUrl = in.readString();
    }

    public static final Creator<BaseMapItem> CREATOR = new Creator<BaseMapItem>() {
        @Override
        public BaseMapItem createFromParcel(Parcel source) {
            return new BaseMapItem(source);
        }

        @Override
        public BaseMapItem[] newArray(int size) {
            return new BaseMapItem[size];
        }
    };
}