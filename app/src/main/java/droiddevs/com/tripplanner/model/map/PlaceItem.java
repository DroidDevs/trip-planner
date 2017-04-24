package droiddevs.com.tripplanner.model.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.model.googleplaces.OpeningHours;
import droiddevs.com.tripplanner.model.googleplaces.Photo;
import droiddevs.com.tripplanner.model.source.remote.PlaceDetailsResponse;
import droiddevs.com.tripplanner.util.PhotoUrlUtil;

/**
 * Created by elmira on 4/17/17.
 */

public class PlaceItem extends BaseMapItem implements Parcelable {

    private float rating;
    private boolean saved;

    private List<Object> weekdayHours;
    private String photoReference;

    private String placeId;
    private String destinationId;

    private String formattedAddress;
    private String phoneNumber;

    public PlaceItem(SavedPlace place, int position) {
        super(new LatLng(place.getLatitude(), place.getLongitude()), place.getName(), position, place.getPhotoUrl());
        this.photoReference = place.getPhotoReference();

        this.rating = (float) place.getRating();
        this.saved = true;

        this.destinationId = place.getDestinationId();
        this.placeId = place.getPlaceId();

        setIconResId(R.drawable.ic_map_saved_place_small);
        setSelectedIconResId(R.drawable.ic_map_saved_place_big);
    }

    public PlaceItem(GooglePlace place, String destinationId, int position, String photoUrl, boolean saved) {
        super(new LatLng(place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng()), place.getName(), position, photoUrl);
        this.rating = place.getRating();
        this.saved = saved;

        this.destinationId = destinationId;
        this.placeId = place.getPlaceId();

        setIconResId(R.drawable.ic_map_suggested_place_small);
        setSelectedIconResId(R.drawable.ic_map_place_color_accent_big);

        OpeningHours openingHours = place.getOpeningHours();
        if (openingHours != null) {
            this.weekdayHours = openingHours.getWeekdayText();
        }
        List<Photo> photos = place.getPhotos();
        if (photos != null && photos.size() > 0) {
            Photo photo = photos.get(0);
            this.photoReference = photo.getPhotoReference();
        }
    }

    public PlaceItem(PlaceDetailsResponse.PlaceDetails placeDetails,
                     String destinationId,
                     PlaceDetailsResponse.PlacePhoto placePhoto) {

        super(new LatLng(placeDetails.getGeometry().getLocation().getLatitude(),
                        placeDetails.getGeometry().getLocation().getLongitude()),
                placeDetails.getName(),
                0,
                placePhoto.getFullPhotoURLReference());


        this.rating = (float) placeDetails.getRating();

        this.saved = false;
        this.destinationId = destinationId;

        this.placeId = placeDetails.getPlaceId();

        this.phoneNumber = placeDetails.getPhoneNumber();
        this.formattedAddress = placeDetails.getAddress();

        List<PlaceDetailsResponse.PlacePhoto> photos = placeDetails.getPhotos();
        if (photos != null && photos.size() > 0) {
            PlaceDetailsResponse.PlacePhoto photo = photos.get(0);
            this.photoReference = photo.getPhotoReference();
        }
    }

    public List<Object> getWeekdayHours() {
        return weekdayHours;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public float getRating() {
        return rating;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getPhotoUrl() {
        return PhotoUrlUtil.getPhotoUrl(photoReference);
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(this.rating);
        dest.writeByte(this.saved ? (byte) 1 : (byte) 0);
        dest.writeList(this.weekdayHours);
        dest.writeString(this.photoReference);
        dest.writeString(this.placeId);
        dest.writeString(this.destinationId);
    }

    protected PlaceItem(Parcel in) {
        super(in);
        this.rating = in.readFloat();
        this.saved = in.readByte() != 0;
        this.weekdayHours = new ArrayList<Object>();
        in.readList(this.weekdayHours, Object.class.getClassLoader());
        this.photoReference = in.readString();
        this.placeId = in.readString();
        this.destinationId = in.readString();
    }

    public static final Creator<PlaceItem> CREATOR = new Creator<PlaceItem>() {
        @Override
        public PlaceItem createFromParcel(Parcel source) {
            return new PlaceItem(source);
        }

        @Override
        public PlaceItem[] newArray(int size) {
            return new PlaceItem[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlaceItem)) {
            return false;
        }
        PlaceItem otherPlaceItem = (PlaceItem) obj;
        return (this.getPlaceId().equals(otherPlaceItem.getPlaceId()))
                && (this.getDestinationId().equals(otherPlaceItem.getDestinationId()));
    }
}
