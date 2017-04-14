package droiddevs.com.tripplanner.model.source.remote;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.FbUser;
import droiddevs.com.tripplanner.model.util.PlacePointConverter;
import droiddevs.com.tripplanner.model.Point;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.DataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elmira on 4/3/17.
 */

public class RemoteDataSource implements DataSource {

    private static final String LOG_TAG = "RemoteDataSource";
    private static RemoteDataSource instance;

    // app based context
    private Context context;

    private GooglePlacesService mGooglePlacesService;

    private RemoteDataSource(Context context, GooglePlacesService googlePlacesService) {
        this.context = context;
        this.mGooglePlacesService = googlePlacesService;
    }

    public static RemoteDataSource getInstance(Context context, GooglePlacesService googlePlacesService) {
        if (instance == null) {
            instance = new RemoteDataSource(context, googlePlacesService);
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
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);
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
                    Log.d(LOG_TAG, "loaded trip with destinations");
                    trip.setDestinations(objects);
                    callback.onTripLoaded(trip);
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

    @Override
    public void updateTrip(Trip trip) {
        if (trip == null) return;
        trip.saveEventually();
    }

    @Override
    public void updateDestination(Destination destination) {
        if (destination == null) return;
        destination.saveEventually();
    }

    @Override
    public void loadCurrentFBUser(final LoadFbUserCallback callback) {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        if (response.getError() != null) {
                            Log.e(LOG_TAG, response.getError().getErrorMessage());
                            callback.onFailure();
                        }
                        else {
                            try {
                                FbUser user = FbUser.fromJsonObject(object);
                                Log.d(LOG_TAG, "Loaded FB user: " + user.toString());
                                callback.onUserLoaded(user);
                            } catch (Throwable ex) {
                                Log.e(LOG_TAG, ex.toString());
                                callback.onFailure();
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", FbJsonAttributes.User.ID + "," + FbJsonAttributes.User.NAME + "," +
                FbJsonAttributes.User.COVER + "," + FbJsonAttributes.User.PICTURE);
        parameters.putString("type", "large");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void deleteTrip(Trip trip, final DeleteTripCallback callback) {
        trip.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                callback.onTripDeleted();
            }
        });
    }

    public void loadFriendsList(String userId) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + userId + "/friendlists",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                    }
                }
        ).executeAsync();
    }

    @Override
    public void loadPlace(final String placeId, final LoadPlaceCallback callback) {
        Call<PlaceDetailsResponse> call = mGooglePlacesService.getPlaceDetails(placeId, context.getString(R.string.google_places_api_key));
        call.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                Log.d(LOG_TAG, "Loaded place from web-api, place id: " + placeId);
                Point place = PlacePointConverter.convertToPoint(response.body());
                Log.d(LOG_TAG, place.toString());
                callback.onPlaceLoaded(place);
            }

            @Override
            public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
                callback.onFailure();
            }
        });
    }
}