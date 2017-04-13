package droiddevs.com.tripplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.addedittrip.AddEditTripActivity;
import droiddevs.com.tripplanner.addedittrip.AddEditTripFragment;
import droiddevs.com.tripplanner.login.OauthActivity;
import droiddevs.com.tripplanner.tripmap.TripMapFragment;

public class MainActivity extends OauthActivity{

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

        /*final Repository repository = TripPlannerApplication.getRepository();
        repository.setCanLoadFromRemoteSource(NetworkUtil.isNetworkAvailable(this));
        repository.loadOpenTrips(new DataSource.LoadTripListCallback() {
            @Override
            public void onTripListLoaded(List<Trip> trips) {
                Log.d(LOG_TAG, "Loaded trips size: " + (trips == null ? 0 : trips.size()));
                if (trips != null && trips.size() > 0) {
                    for (Trip trip : trips) {
                        Log.d(LOG_TAG, "Trip name: " + trip.getName() + ", destinations size: " + (trip.getDestinations() == null ? 0 : trip.getDestinations().size()));
                        repository.loadTripDestinations(trip, new DataSource.LoadTripCallback() {
                            @Override
                            public void onTripLoaded(Trip trip) {
                                if (trip.getDestinations() != null) {
                                    for (Destination dest : trip.getDestinations()) {
                                        Log.d(LOG_TAG, "destination name: " + dest.getName());
                                    }
                                }
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure() {

            }
        });*/

        String tripId = "cdcefb8e-1e28-4d75-8516-658c03758104";

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment == null) {
            fragment = TripMapFragment.newInstance(tripId);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
    }

    public void createNewTrip(View view) {
        Intent intent = new Intent(this, AddEditTripActivity.class);
        startActivity(intent);
    }

    public void updateTrip(View view) {
        Intent intent = new Intent(this, AddEditTripActivity.class);
        intent.putExtra(AddEditTripFragment.ARGUMENT_TRIP_ID, "cdcefb8e-1e28-4d75-8516-658c03758104");
        startActivity(intent);
    }
}
