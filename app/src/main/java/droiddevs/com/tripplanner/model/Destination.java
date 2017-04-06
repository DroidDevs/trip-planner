package droiddevs.com.tripplanner.model;

import com.parse.ParseClassName;

import java.util.Date;
import java.util.List;

/**
 * Created by elmira on 4/4/17.
 */

@ParseClassName("Destination")
public class Destination extends Point {

    public static final String DESTINATION_ID_KEY = "destinationId";
    public static final String TRIP_ID_KEY = "tripId";
    public static final String ORDER_KEY = "order";
    public static final String DURATION_KEY = "duration";
    public static final String START_DATE_KEY = "startDate";
    public static final String END_DATE_KEY = "endDate";
    public static final String SAVED_PLACES_KEY = "savedPlaces";

    public Destination() {
        super();
    }

    public String getDestinationId() {
        return getString(DESTINATION_ID_KEY);
    }

    public void setDestinationId(String destinationId) {
        put(DESTINATION_ID_KEY, destinationId);
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

    public List<Point> getSavedPlaces() {
        return getList(SAVED_PLACES_KEY);
    }

    public void setSavedPlaces(List<Point> savedPlaces) {
        put(SAVED_PLACES_KEY, savedPlaces);
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
                ", savedPlaces=" + getSavedPlaces() +
                '}';
    }
}
