package droiddevs.com.tripplanner.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

/**
 * Created by elmira on 4/3/17.
 */

@ParseClassName("Trip")
public class Trip extends ParseObject {

    public static final String TRIP_ID_KEY = "tripId";
    public static final String NAME_KEY = "name";
    public static final String START_DATE_KEY = "startDate";
    public static final String END_DATE_KEY = "endDate";
    public static final String DESTINATIONS_KEY = "destinations";

    public Trip(){
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

    @Override
    public String toString() {
        return "Trip{" +
                "tripId='" + getTripId() + '\'' +
                ", name='" + getName() + '\'' +
                ", startDate=" + getStartDate() +
                ", endDate=" + getEndDate() +
                '}';
    }
}
