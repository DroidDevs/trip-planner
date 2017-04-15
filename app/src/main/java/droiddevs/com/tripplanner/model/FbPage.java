package droiddevs.com.tripplanner.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by elmira on 4/14/17.
 */

public class FbPage {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("about")
    String about;

    @SerializedName("category")
    String category;

    @SerializedName("cover")
    FbCoverPhoto coverPhoto;

    @SerializedName("location")
    FbLocation location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public FbCoverPhoto getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(FbCoverPhoto coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public FbLocation getLocation() {
        return location;
    }

    public void setLocation(FbLocation location) {
        this.location = location;
    }
}
