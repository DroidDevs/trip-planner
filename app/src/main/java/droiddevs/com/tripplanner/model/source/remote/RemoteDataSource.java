package droiddevs.com.tripplanner.model.source.remote;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.DataSource;

/**
 * Created by elmira on 4/3/17.
 */

public class RemoteDataSource implements DataSource {

    private static final String LOG_TAG = "RemoteDataSource";
    private static RemoteDataSource instance;

    // app based context
    private Context context;

    private RemoteDataSource(Context context) {
        this.context = context;
    }

    public static RemoteDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new RemoteDataSource(context);
        }
        return instance;
    }

    @Override
    public void loadOpenTrips(final LoadTripListCallback callback) {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);
        query.whereGreaterThanOrEqualTo(Trip.END_DATE_KEY, new Date());

        query.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> objects, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailure();
                }
                else {
                    callback.onTripListLoaded(objects);
                }
            }
        });
    }

    @Override
    public void loadTrip(String tripId, final LoadTripCallback callback) {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class).fromLocalDatastore();
        query.whereGreaterThanOrEqualTo(Trip.TRIP_ID_KEY, tripId);
        query.getFirstInBackground(new GetCallback<Trip>() {
            @Override
            public void done(Trip object, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailure();
                }
                else {
                    callback.onTripLoaded(object);
                }
            }
        });
    }

    @Override
    public void updateTrip(final Trip trip, final SaveTripCallback callback) {
         trip.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailed();
                }
                else {
                    Log.d(LOG_TAG, "Trip was updated successfully in remote. Trip Id: " + trip.getTripId());
                    callback.onSuccess();
                }
            }
        });
    }
}