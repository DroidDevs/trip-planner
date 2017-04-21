package droiddevs.com.tripplanner.model.source;

import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.fb.FbUser;
import droiddevs.com.tripplanner.model.source.local.LocalDataSource;
import droiddevs.com.tripplanner.model.source.remote.RemoteDataSource;

/**
 * Created by Elmira Andreeva on 4/3/17.
 */

public class Repository implements DataSource {
    private static final String LOG_TAG = "Repository";

    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    private LinkedHashMap<String, Trip> mCachedUpcomingTrips;
    private static Repository SHARED_INSTANCE;

    private FbUser mCurrentFbUser;
    private HandlerThread mHandlerThread;

    private Handler mBackgroundHandler;
    private Handler mUiHandler;

    public static Repository getInstance(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        if (SHARED_INSTANCE == null) {
            SHARED_INSTANCE = new Repository(localDataSource, remoteDataSource);
        }
        return SHARED_INSTANCE;
    }

    private Repository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;

        mCachedUpcomingTrips = new LinkedHashMap<>();

        mHandlerThread = new HandlerThread("TripPhotoThread", Process.THREAD_PRIORITY_DISPLAY);
        mHandlerThread.start();

        mBackgroundHandler = new Handler(mHandlerThread.getLooper());
        mUiHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void loadUpcomingTrips(final LoadTripListCallback callback) {
        if (!mCachedUpcomingTrips.isEmpty()) {
            callback.onTripListLoaded(new ArrayList<Trip>(mCachedUpcomingTrips.values()));
        }

        //load data from remote data source
        remoteDataSource.loadUpcomingTrips(new LoadTripListCallback() {
            @Override
            public void onTripListLoaded(final List<Trip> trips) {
                Collections.sort(trips);
                addTripsToCache(trips);
                callback.onTripListLoaded(trips);

                if (trips.size() > 0) {
                    ParseObject.unpinAllInBackground(trips, new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseObject.pinAllInBackground(trips);
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
                //load data from local data source
                localDataSource.loadUpcomingTrips(new LoadTripListCallback() {
                    @Override
                    public void onTripListLoaded(List<Trip> trips) {
                        Collections.sort(trips);
                        addTripsToCache(trips);
                        callback.onTripListLoaded(trips);
                    }

                    @Override
                    public void onFailure() {
                        callback.onFailure();
                    }
                });
            }
        });
    }

    @Override
    public void loadPastTrips(final LoadTripListCallback callback) {
        remoteDataSource.loadPastTrips(new LoadTripListCallback() {
            @Override
            public void onTripListLoaded(List<Trip> trips) {
                Collections.sort(trips);
                callback.onTripListLoaded(trips);
            }

            @Override
            public void onFailure() {
                localDataSource.loadPastTrips(callback);
            }
        });
    }

    @Override
    public void loadTrip(final String tripId, final LoadTripCallback callback) {
        Log.d(LOG_TAG, "Loading trip: " + tripId);
        //return Trip immediately if it stores in the cache
        /*if (mCachedUpcomingTrips.containsKey(tripId)) {
            Log.d(LOG_TAG, "Found trip in memory cache");
            callback.onTripLoaded(mCachedUpcomingTrips.get(tripId));
        }*/
        //else {// try to load trip from remote source
        remoteDataSource.loadTrip(tripId, new LoadTripCallback() {
            @Override
            public void onTripLoaded(final Trip trip) {
                Log.d(LOG_TAG, "Found trip in remote source. ");

                boolean isUpcomingTrip = trip.getEndDate().after(new Date());
                if (isUpcomingTrip) {
                    mCachedUpcomingTrips.put(tripId, trip);
                }
                callback.onTripLoaded(trip);

                //sync with local data source
                trip.unpinInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        trip.pinInBackground();
                    }
                });
            }

            @Override
            public void onFailure() {
                // try to load trip from local source
                localDataSource.loadTrip(tripId, new LoadTripCallback() {
                    @Override
                    public void onTripLoaded(final Trip trip) {
                        Log.d(LOG_TAG, "Found trip in local database.");
                        boolean isUpcomingTrip = trip.getEndDate().after(new Date());
                        if (isUpcomingTrip) {
                            mCachedUpcomingTrips.put(tripId, trip);
                        }
                        callback.onTripLoaded(trip);
                    }

                    @Override
                    public void onFailure() {
                        callback.onFailure();
                    }
                });
            }
        });
        //}
    }

    @Override
    public void updateTrip(final Trip trip, final SaveTripCallback callback) {
        final boolean isUpcomingTrip = trip.getEndDate().after(new Date());
        //update memory cache
        if (isUpcomingTrip) {
            mCachedUpcomingTrips.put(trip.getTripId(), trip);
        }
        //update local database
        localDataSource.updateTrip(trip, new SaveTripCallback() {
            @Override
            public void onSuccess(Trip savedTrip) {
                remoteDataSource.updateTrip(trip);

                if (trip.getDestinations() == null || trip.getDestinations().size() == 0) {
                    callback.onSuccess(trip);
                    return;
                }

                // Download all destinations photos in background
                mBackgroundHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        boolean loadedTripPhoto = false;
                        for (final Destination destination : trip.getDestinations()) {
                            if (destination.getPhotoReference() == null || "".equals(destination.getPhotoReference())) {
                                SavedPlace place = remoteDataSource.loadPlaceSynchronously(destination.getPlaceId());
                                if (place != null && place.getPhotoReference() != null) {
                                    destination.setPhotoReference(place.getPhotoReference());
                                    updateDestination(destination);
                                }
                            }
                            //update trip photo and notify callback on first loaded destination photo
                            if (!loadedTripPhoto && destination.getPhotoReference() != null) {
                                loadedTripPhoto = true;
                                trip.setPhotoReference(destination.getPhotoReference());
                                updateTrip(trip);
                                if (isUpcomingTrip) {
                                    mCachedUpcomingTrips.put(trip.getTripId(), trip);
                                }

                                // Execute callback on main thread
                                mUiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(trip);
                                    }
                                });
                            }
                        }
                        if (isUpcomingTrip) {
                            mCachedUpcomingTrips.put(trip.getTripId(), trip);
                        }
                    }
                });
            }

            @Override
            public void onFailed() {
                callback.onFailed();
            }
        });
    }

    @Override
    public void updateTrip(Trip trip) {
        //update local database
        localDataSource.updateTrip(trip);
        //update remotely
        remoteDataSource.updateTrip(trip);
    }

    @Override
    public void loadTripDestinations(final Trip trip, final LoadTripCallback callback) {
        remoteDataSource.loadTripDestinations(trip, new LoadTripCallback() {
            @Override
            public void onTripLoaded(Trip trip) {
                callback.onTripLoaded(trip);

                //sync with local data source
                ParseObject.unpinAllInBackground(trip.getDestinations());
                ParseObject.pinAllInBackground(trip.getDestinations());
            }

            @Override
            public void onFailure() {
                localDataSource.loadTripDestinations(trip, callback);
            }
        });
    }

    @Override
    public void loadDestination(final String destinationId, final LoadDestinationCallback callback) {
        remoteDataSource.loadDestination(destinationId, new LoadDestinationCallback() {
            @Override
            public void onDestinationLoaded(Destination destination) {
                callback.onDestinationLoaded(destination);

                //sync with local data source
                destination.unpinInBackground();
                destination.pinInBackground();
            }

            @Override
            public void onFailure() {
                localDataSource.loadDestination(destinationId, callback);
            }
        });
    }

    @Override
    public void loadCurrentFBUser(LoadFbUserCallback callback) {
        remoteDataSource.loadCurrentFBUser(new LoadFbUserCallback() {
            @Override
            public void onUserLoaded(FbUser user) {
                mCurrentFbUser = user;
            }

            @Override
            public void onFailure() {
                // do nothing
            }
        });
    }

    @Override
    public void searchFbPlaces(Location location, int radiusInMeters, int resultsLimit, String searchText, SearchFbPlacesCallback callback) {
        remoteDataSource.searchFbPlaces(location, radiusInMeters, resultsLimit, searchText, callback);
    }

    @Override
    public void searchGooglePlaces(String location, int radiusInMeters, String searchText, String apiKey, SearchGooglePlacesCallback callback) {
        remoteDataSource.searchGooglePlaces(location, radiusInMeters, searchText, apiKey, callback);
    }

    private void addTripsToCache(List<Trip> list) {
        mCachedUpcomingTrips.clear();
        if (list == null || list.size() == 0) return;

        for (Trip trip : list) {
            mCachedUpcomingTrips.put(trip.getTripId(), trip);
        }
    }

    public boolean isCurrentFbUserDefined() {
        Log.d(LOG_TAG, "current FB user: " + (mCurrentFbUser == null ? "undefined" : mCurrentFbUser.toString()));
        return mCurrentFbUser != null;
    }

    public FbUser getCurrentFbUser() {
        return mCurrentFbUser;
    }

    @Override
    public void loadPlace(final String placeId, final LoadPlaceCallback callback) {
        localDataSource.loadPlace(placeId, new LoadPlaceCallback() {
            @Override
            public void onPlaceLoaded(SavedPlace place) {
                if (place != null) {
                    callback.onPlaceLoaded(place);
                }
                else {
                    remoteDataSource.loadPlace(placeId, callback);
                }
            }

            @Override
            public void onFailure() {
                remoteDataSource.loadPlace(placeId, callback);
            }
        });
    }

    @Override
    public SavedPlace loadPlaceSynchronously(String placeId) {
        throw new UnsupportedOperationException("Operation is only supported in remote data source");
    }

    @Override
    public void updateDestination(Destination destination) {
        //update local database
        localDataSource.updateDestination(destination);
        //update remotely
        remoteDataSource.updateDestination(destination);
    }

    @Override
    public void deleteTrip(final Trip trip, final DeleteTripCallback callback) {
        mCachedUpcomingTrips.remove(trip.getTripId());
        localDataSource.deleteTrip(trip, new DeleteTripCallback() {
            @Override
            public void onTripDeleted() {
                callback.onTripDeleted();
                remoteDataSource.deleteTrip(trip, null);
            }
        });
    }

    @Override
    public void loadSavedPlaces(final String destinationId, final LoadSavedPlacesCallback callback) {
        //load data from remote data source
        remoteDataSource.loadSavedPlaces(destinationId, new LoadSavedPlacesCallback() {
            @Override
            public void onSavedPlacesLoaded(List<SavedPlace> places) {
                callback.onSavedPlacesLoaded(places);
                ParseObject.unpinAllInBackground(places);
                ParseObject.pinAllInBackground(places);
            }

            @Override
            public void onFailure() {
                //load data from local data source
                localDataSource.loadSavedPlaces(destinationId, new LoadSavedPlacesCallback() {
                    @Override
                    public void onSavedPlacesLoaded(List<SavedPlace> places) {
                        callback.onSavedPlacesLoaded(places);
                    }

                    @Override
                    public void onFailure() {
                        callback.onFailure();
                    }
                });
            }
        });
    }

    @Override
    public void loadSavedPlace(final String googlePlaceId, final String destinationId, final LoadSavedPlaceCallback callback) {
        remoteDataSource.loadSavedPlace(googlePlaceId, destinationId, new LoadSavedPlaceCallback() {
            @Override
            public void onSavedPlaceLoaded(SavedPlace place) {
                if (place != null) {
                    place.unpinInBackground();
                    place.pinInBackground();
                }
                callback.onSavedPlaceLoaded(place);
            }

            @Override
            public void onFailure() {
                localDataSource.loadSavedPlace(googlePlaceId, destinationId, callback);
            }
        });
    }

    @Override
    public void deleteSavedPlace(final SavedPlace savedPlace, final DeleteSavedPlaceCallback callback) {
        localDataSource.deleteSavedPlace(savedPlace, new DeleteSavedPlaceCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
                remoteDataSource.deleteSavedPlace(savedPlace, null);
            }

            @Override
            public void onFailed() {
                callback.onFailed();
            }
        });

    }

    @Override
    public void createSavedPlace(final SavedPlace savedPlace, final CreateSavedPlaceCallback callback) {
        localDataSource.createSavedPlace(savedPlace, new CreateSavedPlaceCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
                remoteDataSource.createSavedPlace(savedPlace, null);
            }

            @Override
            public void onFailed() {
                callback.onFailed();
            }
        });
    }

    public void destroy() {
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
    }
}
