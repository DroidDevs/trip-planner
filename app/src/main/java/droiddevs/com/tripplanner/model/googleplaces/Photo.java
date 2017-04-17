
package droiddevs.com.tripplanner.model.googleplaces;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import droiddevs.com.tripplanner.application.TripPlannerApplication;

public class Photo implements Parcelable
{

    @SerializedName("height")
    @Expose
    private Long height;
    @SerializedName("html_attributions")
    @Expose
    private List<String> htmlAttributions = null;
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
            instance.height = ((Long) in.readValue((Long.class.getClassLoader())));
            in.readList(instance.htmlAttributions, (java.lang.String.class.getClassLoader()));
            instance.photoReference = ((String) in.readValue((String.class.getClassLoader())));
            instance.width = ((Long) in.readValue((Long.class.getClassLoader())));
            return instance;
        }

        public Photo[] newArray(int size) {
            return (new Photo[size]);
        }

    }
    ;

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
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&photoreference=" + photoReference + "&key=" + TripPlannerApplication.getGooglePlacesApiKey();
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
        dest.writeValue(height);
        dest.writeList(htmlAttributions);
        dest.writeValue(photoReference);
        dest.writeValue(width);
    }

    public int describeContents() {
        return  0;
    }

}
