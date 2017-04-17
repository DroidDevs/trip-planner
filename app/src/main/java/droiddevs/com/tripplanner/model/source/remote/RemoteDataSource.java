package droiddevs.com.tripplanner.model.source.remote;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.fb.FbPlace;
import droiddevs.com.tripplanner.model.fb.FbUser;
import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.util.PlaceConverter;
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

    public String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    Gson gson = null;

    private RemoteDataSource(Context context, GooglePlacesService googlePlacesService) {
        this.context = context;
        this.mGooglePlacesService = googlePlacesService;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(ISO_FORMAT);
        gson = gsonBuilder.create();
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
    public void loadPastTrips(final LoadTripListCallback callback) {
        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);
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
    public void loadDestination(String destinationId, final LoadDestinationCallback callback) {
        if (destinationId == null) {
            callback.onFailure();
            return;
        }

        ParseQuery<Destination> query = ParseQuery.getQuery(Destination.class);
        query.whereEqualTo(Destination.DESTINATION_ID_KEY, destinationId);
        query.getFirstInBackground(new GetCallback<Destination>() {
            @Override
            public void done(Destination object, ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    callback.onFailure();
                }
                else {
                    Log.e(LOG_TAG, "loaded destination");
                    callback.onDestinationLoaded(object);
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
                                //FbUser user = FbUser.fromJsonObject(object);
                                Log.d(LOG_TAG, "FB raw response: " + response.getRawResponse());
                                FbUser user = gson.fromJson(response.getRawResponse(), FbUser.class);
                                Log.d(LOG_TAG, "Loaded FB user: " + user.toString());
                                callback.onUserLoaded(user);
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                                Log.e(LOG_TAG, ex.toString());
                                callback.onFailure();
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", FbJsonAttributes.User.ID + "," + FbJsonAttributes.User.NAME + "," +
                FbJsonAttributes.User.COVER + "," + FbJsonAttributes.User.PICTURE);
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

    @Override
    public void loadPlace(final String placeId, final LoadPlaceCallback callback) {
        Call<PlaceDetailsResponse> call = mGooglePlacesService.getPlaceDetails(placeId, context.getString(R.string.google_places_api_key));
        call.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                Log.d(LOG_TAG, "Loaded place from web-api, place id: " + placeId);
                SavedPlace place = PlaceConverter.convertToSavedPlace(response.body());
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

    @Override
    public SavedPlace loadPlaceSynchronously(final String placeId) {
        Call<PlaceDetailsResponse> call =
                mGooglePlacesService.getPlaceDetails(placeId, context.getString(R.string.google_places_api_key));
        try {
            Response<PlaceDetailsResponse> response = call.execute();
            if (response.isSuccessful()) {
                Log.d(LOG_TAG, "Loaded place from web-api, place id: " + placeId);
                SavedPlace place = PlaceConverter.convertToSavedPlace(response.body());
                return place;
            } else {
                return null;
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public void searchFbPlaces(Location location, int radiusInMeters, int resultsLimit, String searchText, final SearchFbPlacesCallback callback) {
        GraphRequest searchRequest = GraphRequest.newPlacesSearchRequest(
                AccessToken.getCurrentAccessToken(),
                location, radiusInMeters, resultsLimit, searchText,
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray objects, GraphResponse response) {
                        if (response.getError() != null) {
                            Log.e(LOG_TAG, response.getError().getErrorMessage());
                            callback.onFailure();
                        }
                        else {
                            try {
                                Log.d(LOG_TAG, "FB raw response: " + response.getRawResponse());
                                JSONObject responseObject = response.getJSONObject();
                                JSONArray placesArray = responseObject.getJSONArray("data");

                                List<FbPlace> places = gson.fromJson(placesArray.toString(), new TypeToken<Collection<FbPlace>>() {
                                }.getType());
                                callback.onPlacesFound(places);
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                                Log.e(LOG_TAG, ex.toString());
                                callback.onFailure();
                            }
                        }
                    }
                });
        searchRequest.executeAsync();
    }

    public void searchGooglePlaces(String location, int radiusInMeters, String searchTypeString, String apiKey, final SearchGooglePlacesCallback callback) {
        GooglePlacesService retrofitService = RetrofitGooglePlacesService.newGooglePlacesService();

        Call<List<GooglePlace>> listCall = retrofitService.searchPlaces(location, radiusInMeters, searchTypeString, apiKey);
        listCall.enqueue(new Callback<List<GooglePlace>>() {
            @Override
            public void onResponse(Call<List<GooglePlace>> call, Response<List<GooglePlace>> response) {
                if (response != null) {
                    callback.onPlacesFound(response.body());
                }
                else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<GooglePlace>> call, Throwable t) {
                Log.e(LOG_TAG, t.getLocalizedMessage());
                callback.onFailure();
            }
        });
    }

    @Override
    public void loadSavedPlaces(String destinationId, final LoadSavedPlacesCallback callback) {
        ParseQuery<SavedPlace> query = ParseQuery.getQuery(SavedPlace.class);
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
        ParseQuery<SavedPlace> query = ParseQuery.getQuery(SavedPlace.class);
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
        savedPlace.deleteEventually(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    if (callback != null) {
                        callback.onFailed();
                    }
                }
                else {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                }
            }
        });
    }

    @Override
    public void createSavedPlace(SavedPlace savedPlace, final CreateSavedPlaceCallback callback) {
        savedPlace.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(LOG_TAG, e.toString());
                    if (callback != null) {
                        callback.onFailed();
                    }
                }
                else {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                }
            }
        });
    }
}