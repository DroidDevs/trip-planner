package droiddevs.com.tripplanner.model;

import android.support.annotation.NonNull;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

import droiddevs.com.tripplanner.util.PhotoUrlUtil;

/**
 * Created by elmira on 4/3/17.
 */

@ParseClassName("Trip")
public class Trip extends ParseObject implements Comparable<Trip>{

    public static final String TRIP_ID_KEY = "tripId";
    public static final String NAME_KEY = "name";
    public static final String START_DATE_KEY = "startDate";
    public static final String END_DATE_KEY = "endDate";
    public static final String DESTINATIONS_KEY = "destinations";
    public static final String PHOTO_REFERENCE_KEY = "photoReference";

    public Trip() {
        super();
    }

    public String getTripId() {
        return getString(TRIP_ID_KEY);
    }

    public void setTripId(String tripId) {
        put(TRIP_ID_KEY, tripId);
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }

    public Date getStartDate() {
        return getDate(START_DATE_KEY);
    }

    public void setStartDate(Date startDate) {
        put(START_DATE_KEY, startDate);
    }

    public Date getEndDate() {
        return getDate(END_DATE_KEY);
    }

    public void setEndDate(Date endDate) {
        put(END_DATE_KEY, endDate);
    }

    public List<Destination> getDestinations() {
        return getList(DESTINATIONS_KEY);
    }

    public void setDestinations(List<Destination> destinations) {
        put(DESTINATIONS_KEY, destinations);
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

    public String getPhotoUrl() {
        return PhotoUrlUtil.getPhotoUrl(getPhotoReference());
    }

    @Override
    public int hashCode() {
        return getTripId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Trip)) return false;
        return this.getTripId().equals(((Trip) obj).getTripId());
    }

    @Override
    public int compareTo(@NonNull Trip o) {
        if (this.getTripId().equals(o.getTripId())) return 0;
        return getStartDate().compareTo(o.getStartDate());
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId='" + getTripId() + '\'' +
                ", name='" + getName() + '\'' +
                ", photoReference='" + getPhotoReference() + '\'' +
                ", startDate=" + getStartDate() +
                ", endDate=" + getEndDate() +
                '}';
    }
}
