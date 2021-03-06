package droiddevs.com.tripplanner.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import droiddevs.com.tripplanner.util.PhotoUrlUtil;

/**
 * Created by elmira on 4/4/17.
 */

@ParseClassName("SavedPlace")
public class SavedPlace extends ParseObject {

    public static final String NAME_KEY = "name";
    public static final String PLACE_ID_KEY = "placeId";
    public static final String DESTINATION_ID_KEY = "destinationId";
    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";
    public static final String RATING_KEY = "rating";
    public static final String PHONE_KEY = "phoneNumber";
    public static final String TYPES_KEY = "types";
    public static final String PHOTO_REFERENCE_KEY = "photoReference";
    public static final String OPEN_NOW_KEY = "openNow";
    public static final String OPEN_NOW_WEEKDAY_TEXT_KEY = "openNowWeekdayText";

    public SavedPlace() {
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

    public void setPlaceId(String pointId) {
        put(PLACE_ID_KEY, pointId);
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

    public List<String> getTypes() {
        return getList(TYPES_KEY);
    }

    public void setTypes(List<String> types) {
        put(TYPES_KEY, types);
    }

    public String getDestinationId() {
        return getString(DESTINATION_ID_KEY);
    }

    public void setDestinationId(String destinationId) {
        put(DESTINATION_ID_KEY, destinationId);
    }

    public List<Object> getOpenNowWeekdayText() {
        return getList(OPEN_NOW_WEEKDAY_TEXT_KEY);
    }

    public void setOpenNowWeekdayText(List<Object> weekdayText) {
        put(OPEN_NOW_WEEKDAY_TEXT_KEY, weekdayText);
    }

    public boolean getOpenNow() {
        return getBoolean(OPEN_NOW_KEY);
    }

    public void setOpenNow(boolean openNow) {
        put(OPEN_NOW_KEY, openNow);
    }

    public String getPhotoUrl() {
        return PhotoUrlUtil.getPhotoUrl(getPhotoReference());
    }

    public String getPhotoReference() {
        try {
            return fetchIfNeeded().getString(PHOTO_REFERENCE_KEY);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setPhotoReference(String photoReference) {
        put(PHOTO_REFERENCE_KEY, photoReference);
    }

    @Override
    public String toString() {
        return "SavedPlace{" +
                "name='" + getName() + '\'' +
                ", pointId='" + getPlaceId() + '\'' +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", rating=" + getRating() +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", photoUrl='" + getPhotoUrl() + '\'' +
                ", types=" + getTypes() +
                '}';
    }
}
