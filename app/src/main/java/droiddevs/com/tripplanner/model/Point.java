package droiddevs.com.tripplanner.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by elmira on 4/4/17.
 */

@ParseClassName("Place")
public class Place extends ParseObject {

    public static final String NAME_KEY = "name";
    public static final String PLACE_ID_KEY = "placeId";
    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";
    public static final String RATING_KEY = "rating";
    public static final String PHONE_KEY = "phoneNumber";
    public static final String TYPES_KEY = "types";

    public Place(){
        super();
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }

    public String getPlaceId() {
        return getString(PLACE_ID_KEY);
    }

    public void setPlaceId(String placeId) {
        put(PLACE_ID_KEY, placeId);
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

    @Override
    public String toString() {
        return "Place{" +
                "name='" + getName() + '\'' +
                ", placeId='" + getPlaceId() + '\'' +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", rating=" + getRating() +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", types=" + getTypes() +
                '}';
    }
}