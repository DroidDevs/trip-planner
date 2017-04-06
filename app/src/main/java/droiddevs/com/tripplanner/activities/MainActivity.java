package droiddevs.com.tripplanner.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Trip;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for user authentication
        /*if (AccessToken.getCurrentAccessToken() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }*/

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 2);

        createParseDataRemote(calendar.getTime());
        createParseDataLocally(calendar.getTime());

        getParseDataLocally();
        getParseDataRemote();
    }

    private void createParseDataRemote(Date endDate) {

        Trip trip = ParseObject.create(Trip.class);
        String uid = UUID.randomUUID().toString();
        trip.setName("Trip Remote " + uid);
        trip.setTripId(uid);
        trip.setStartDate(new Date());
        trip.setEndDate(endDate);

        trip.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(LOG_TAG, "Trip was successfully saved remotely!");
                }
                else {
                    Log.e(LOG_TAG, e.toString());
                }
            }
        });
    }

    private void createParseDataLocally(Date endDate) {
        Trip trip = ParseObject.create(Trip.class);
        String uid = UUID.randomUUID().toString();
        trip.setName("Trip Local " + uid);
        trip.setTripId(uid);
        trip.setStartDate(new Date());
        trip.setEndDate(endDate);

        trip.pinInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(LOG_TAG, "Trip was successfully saved locally!");
                }
                else {
                    Log.e(LOG_TAG, e.toString());
                }
            }
        });
    }

    private void getParseDataLocally() {
        Log.d(LOG_TAG, "Get data from local data source");
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class).fromLocalDatastore();

        query.whereGreaterThanOrEqualTo(Trip.END_DATE_KEY, new Date());
        query.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> objects, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                }
                else {
                    Log.d(LOG_TAG, "local trip size: " + objects.size());
                    for (Trip trip : objects) {
                        Log.d(LOG_TAG, trip.toString());
                        trip.setName("Updated " + trip.getName());
                        trip.pinInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(LOG_TAG, e.toString());
                                }
                                else {
                                    Log.d(LOG_TAG, "Trip was successfully updated locally!");
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void getParseDataRemote() {
        Log.d(LOG_TAG, "Get data from remote data source");
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);
        query.whereGreaterThanOrEqualTo(Trip.END_DATE_KEY, new Date());

        query.findInBackground(new FindCallback<Trip>() {
            @Override
            public void done(List<Trip> objects, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                }
                else {
                    Log.d(LOG_TAG, "remote trip size: " + objects.size());
                    for (Trip trip : objects) {
                        Log.d(LOG_TAG, trip.toString());

                        trip.setName("Updated " + trip.getName());
                        trip.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(LOG_TAG, e.toString());
                                }
                                else {
                                    Log.d(LOG_TAG, "Trip was successfully updated remotely!");
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
