package droiddevs.com.tripplanner.model;

import android.support.annotation.NonNull;

import com.parse.ParseClassName;

import java.util.Date;

/**
 * Created by elmira on 4/4/17.
 */

@ParseClassName("Destination")
public class Destination extends SavedPlace implements Comparable<Destination> {

    public static final String TRIP_ID_KEY = "tripId";
    public static final String ORDER_KEY = "order";
    public static final String DURATION_KEY = "duration";
    public static final String START_DATE_KEY = "startDate";
    public static final String END_DATE_KEY = "endDate";

    public Destination() {
        super();
    }

    public String getTripId() {
        return getString(TRIP_ID_KEY);
    }

    public void setTripId(String tripId) {
        put(TRIP_ID_KEY, tripId);
    }

    public int getOrder() {
        return getInt(ORDER_KEY);
    }

    public void setOrder(int order) {
        put(ORDER_KEY, order);
    }

    public int getDuration() {
        return getInt(DURATION_KEY);
    }

    public void setDuration(int duration) {
        put(DURATION_KEY, duration);
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

    @Override
    public String toString() {
        return "Destination{" +
                "destinationId='" + getDestinationId() + '\'' +
                ", tripId='" + getTripId() + '\'' +
                ", name='" + getName() + '\'' +
                ", order=" + getOrder() +
                ", duration=" + getDuration() +
                ", startDate=" + getStartDate() +
                ", endDate=" + getEndDate() +
                '}';
    }

    @Override
    public int compareTo(@NonNull Destination o) {
        if (this.getDestinationId().equals(o.getDestinationId())) return 0;
        return this.getOrder() < o.getOrder() ? -1 : 1;
    }
}
