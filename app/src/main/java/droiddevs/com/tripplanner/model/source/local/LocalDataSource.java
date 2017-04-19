package droiddevs.com.tripplanner.model.source.local;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.DataSource;

/**
 * Created by elmira on 4/3/17.
 */

public class LocalDataSource implements DataSource {

    private static final String LOG_TAG = "LocalDataSource";

    private static LocalDataSource instance;

    // app based context
    private Context context;

    private LocalDataSource(Context context) {
        this.context = context;
    }

    public static LocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new LocalDataSource(context);
        }
        return instance;
    }

    @Override
    public void loadOpenTrips(final LoadTripListCallback callback) {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class).fromLocalDatastore();
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
    public void loadPastTrips(final LoadTripListCallback callback) {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class).fromLocalDatastore();
        query.whereLessThanOrEqualTo(Trip.END_DATE_KEY, new Date());

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
        query.whereEqualTo(Trip.TRIP_ID_KEY, tripId);
        query.getFirstInBackground(new GetCallback<Trip>() {
            @Override
            public void done(final Trip trip, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailure();
                }
                else {
                    loadTripDestinations(trip, callback);
                }
            }
        });
    }

    @Override
    public void loadTripDestinations(final Trip trip, final LoadTripCallback callback) {
        if (trip == null) {
            callback.onFailure();
            return;
        }

        trip.fetchAllInBackground(trip.getDestinations(), new FindCallback<Destination>() {
            @Override
            public void done(List<Destination> objects, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailure();
                }
                else {
                    Collections.sort(objects);
                    trip.setDestinations(objects);
                    callback.onTripLoaded(trip);
                }
            }
        });
    }

    @Override
    public void loadDestination(String destinationId, final LoadDestinationCallback callback) {
        if (destinationId == null) {
            callback.onFailure();
            return;
        }

        ParseQuery<Destination> query = ParseQuery.getQuery(Destination.class).fromLocalDatastore();
        query.whereEqualTo(Destination.DESTINATION_ID_KEY, destinationId);
        query.getFirstInBackground(new GetCallback<Destination>() {
            @Override
            public void done(Destination object, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailure();
                }
                else {
                    Log.e(LOG_TAG, "loaded destination locally");
                    callback.onDestinationLoaded(object);
                }
            }
        });
    }

    @Override
    public void updateTrip(final Trip trip, final SaveTripCallback callback) {
        trip.pinInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailed();
                }
                else {
                    Log.d(LOG_TAG, "Trip was updated successfully locally, tripId: " + trip.getTripId());
                    callback.onSuccess();
                }
            }
        });
    }

    @Override
    public void updateTrip(Trip trip) {
        if (trip == null) return;
        trip.pinInBackground();
    }

    @Override
    public void loadCurrentFBUser(LoadFbUserCallback callback) {
        throw new UnsupportedOperationException("Operation is not supported in local data source");
    }

    @Override
    public void searchFbPlaces(Location location, int radiusInMeters, int resultsLimit, String searchText, SearchFbPlacesCallback callback) {
        throw new UnsupportedOperationException("Operation is not supported in local data source");
    }

    @Override
    public void searchGooglePlaces(String location, int radiusInMeters, String searchText, String apiKey, SearchGooglePlacesCallback callback) {
        throw new UnsupportedOperationException("Operation is not supported in local data source");
    }

    @Override
    public void loadPlace(String placeId, final LoadPlaceCallback callback) {
        ParseQuery<SavedPlace> query = ParseQuery.getQuery(SavedPlace.class).fromLocalDatastore();
        query.whereEqualTo(SavedPlace.PLACE_ID_KEY, placeId);
        query.getFirstInBackground(new GetCallback<SavedPlace>() {
            @Override
            public void done(final SavedPlace place, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailure();
                }
                else {
                    callback.onPlaceLoaded(place);
                }
            }
        });
    }

    public SavedPlace loadPlaceSynchronously(final String placeId) {
        throw new UnsupportedOperationException("Operation is not supported in local data source");
    }

    @Override
    public void updateDestination(Destination destination) {
        if (destination == null) return;
        destination.pinInBackground();
    }

    @Override
    public void deleteTrip(Trip trip, final DeleteTripCallback callback) {
        trip.unpinInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                callback.onTripDeleted();
            }
        });
    }

    @Override
    public void loadSavedPlaces(String destinationId, final LoadSavedPlacesCallback callback) {
        ParseQuery<SavedPlace> query = ParseQuery.getQuery(SavedPlace.class).fromLocalDatastore();
        query.whereEqualTo(SavedPlace.DESTINATION_ID_KEY, destinationId);

        query.findInBackground(new FindCallback<SavedPlace>() {
            @Override
            public void done(List<SavedPlace> objects, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailure();
                }
                else {
                    callback.onSavedPlacesLoaded(objects);
                }
            }
        });
    }

    @Override
    public void loadSavedPlace(String googlePlaceId, String destinationId, final LoadSavedPlaceCallback callback) {
        ParseQuery<SavedPlace> query = ParseQuery.getQuery(SavedPlace.class).fromLocalDatastore();
        query.whereEqualTo(SavedPlace.PLACE_ID_KEY, googlePlaceId);
        query.whereEqualTo(SavedPlace.DESTINATION_ID_KEY, destinationId);

        query.getFirstInBackground(new GetCallback<SavedPlace>() {
            @Override
            public void done(SavedPlace object, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailure();
                } else {
                    callback.onSavedPlaceLoaded(object);
                }
            }
        });
    }

    @Override
    public void deleteSavedPlace(SavedPlace savedPlace, final DeleteSavedPlaceCallback callback) {
        savedPlace.unpinInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailed();
                }
                else {
                    callback.onSuccess();
                }
            }
        });
    }

    @Override
    public void createSavedPlace(SavedPlace savedPlace, final CreateSavedPlaceCallback callback) {
        savedPlace.pinInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailed();
                }
                else {
                    callback.onSuccess();
                }
            }
        });
    }
}