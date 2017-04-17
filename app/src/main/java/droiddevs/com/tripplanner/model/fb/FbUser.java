package droiddevs.com.tripplanner.model.fb;

import org.json.JSONException;
import org.json.JSONObject;

import droiddevs.com.tripplanner.model.source.remote.FbJsonAttributes;

/**
 * Created by elmira on 4/12/17.
 */

public class FbUser {

    private String id;

    private String name;

    private String coverPhotoUrl;

    private String pictureUrl;

    public static FbUser fromJsonObject(JSONObject jsonObject) throws JSONException {
        FbUser user = new FbUser();
        user.name = jsonObject.getString(FbJsonAttributes.User.NAME);
        user.id = jsonObject.getString(FbJsonAttributes.User.ID);
        if (jsonObject.has(FbJsonAttributes.User.COVER)) {
            user.coverPhotoUrl = jsonObject.getJSONObject(FbJsonAttributes.User.COVER).getString(FbJsonAttributes.CoverPhoto.SOURCE);
        }
        if (jsonObject.has(FbJsonAttributes.User.PICTURE)) {
            user.pictureUrl = jsonObject.getJSONObject(FbJsonAttributes.User.PICTURE).getJSONObject(FbJsonAttributes.DATA).getString(FbJsonAttributes.ProfilePictureSource.URL);
        }
        return user;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public String toString() {
        return "FbUser{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pictureUrl='" + getPictureUrl() + "\'" +
                ", coverPhotoUrl='" + getCoverPhotoUrl() + '\'' +
                '}';
    }
}
