package droiddevs.com.tripplanner.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

import droiddevs.com.tripplanner.application.TripPlannerApplication;

/**
 * Created by elmira on 4/4/17.
 */

@ParseClassName("Point")
public class Point extends ParseObject {

    public static final String NAME_KEY = "name";
    public static final String POINT_ID_KEY = "pointId";
    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";
    public static final String RATING_KEY = "rating";
    public static final String PHONE_KEY = "phoneNumber";
    public static final String TYPES_KEY = "types";
    public static final String PHOTO_REFERENCE_KEY = "photoReference";

    public Point() {
        super();
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }

    public String getPointId() {
        return getString(POINT_ID_KEY);
    }

    public void setPointId(String pointId) {
        put(POINT_ID_KEY, pointId);
    }

    public double getLatitude() {
        return getDouble(LATITUDE_KEY);
    }

    public void setLatitude(double latitude) {
        put(LATITUDE_KEY, latitude);
    }

    public double getLongitude() {
        return getDouble(LONGITUDE_KEY);
    }

    public void setLongitude(double longitude) {
        put(LONGITUDE_KEY, longitude);
    }

    public double getRating() {
        return getDouble(RATING_KEY);
    }

    public void setRating(double rating) {
        put(RATING_KEY, rating);
    }

    public String getPhoneNumber() {
        return getString(PHONE_KEY);
    }

    public void setPhoneNumber(String phoneNumber) {
        put(PHONE_KEY, phoneNumber);
    }

    public List<Integer> getTypes() {
        return getList(TYPES_KEY);
    }

    public void setTypes(List<Integer> types) {
        put(TYPES_KEY, types);
    }

    public String getPhotoUrl() {
        String photoReference = getPhotoReference();
        if (photoReference == null || "".equals(photoReference.trim())) return null;
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&photoreference=" + photoReference + "&key=" + TripPlannerApplication.getGooglePlacesApiKey();
    }

    public String getPhotoReference() {
        return getString(PHOTO_REFERENCE_KEY);
    }

    public void setPhotoReference(String photoReference) {
        put(PHOTO_REFERENCE_KEY, photoReference);
    }

    @Override
    public String toString() {
        return "Point{" +
                "name='" + getName() + '\'' +
                ", pointId='" + getPointId() + '\'' +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", rating=" + getRating() +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", photoUrl='" + getPhotoUrl() + '\'' +
                ", types=" + getTypes() +
                '}';
    }
}
