package droiddevs.com.tripplanner.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by elmira on 4/14/17.
 */

public class FbCoverPhoto {

    @SerializedName("id")
    String id;

    @SerializedName("source")
    String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
