
package droiddevs.com.tripplanner.model.googleplaces;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.util.PhotoUrlUtil;

public class Photo implements Parcelable
{

    @SerializedName("height")
    @Expose
    private Long height;
    @SerializedName("html_attributions")
    @Expose
    private List<String> htmlAttributions = new ArrayList<>();
    @SerializedName("photo_reference")
    @Expose
    private String photoReference;
    @SerializedName("width")
    @Expose
    private Long width;

    public final static Parcelable.Creator<Photo> CREATOR = new Creator<Photo>() {
        @SuppressWarnings({
            "unchecked"
        })
        public Photo createFromParcel(Parcel in) {
            Photo instance = new Photo();
            instance.height = in.readLong();
            instance.photoReference = in.readString();
            instance.width = in.readLong();
            return instance;
        }

        public Photo[] newArray(int size) {
            return (new Photo[size]);
        }

    };

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public String getFullPhotoURLReference() {
        return PhotoUrlUtil.getPhotoUrl(photoReference);
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(height);
        dest.writeString(photoReference);
        dest.writeLong(width);
    }

    public int describeContents() {
        return  0;
    }

}
