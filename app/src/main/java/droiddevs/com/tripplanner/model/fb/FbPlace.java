package droiddevs.com.tripplanner.model.fb;

import com.google.gson.annotations.SerializedName;

/**
 * Created by elmira on 4/14/17.
 */

public class FbPlace {
    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("location")
    FbLocation location;

    @SerializedName("overall_rating")
    double rating;

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

    public FbLocation getLocation() {
        return location;
    }

    public void setLocation(FbLocation location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
