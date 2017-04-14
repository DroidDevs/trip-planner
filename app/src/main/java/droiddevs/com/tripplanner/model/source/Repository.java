package droiddevs.com.tripplanner.model.source;

import android.util.Log;

import java.util.LinkedHashMap;
import java.util.List;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.FbUser;
import droiddevs.com.tripplanner.model.Point;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.local.LocalDataSource;
import droiddevs.com.tripplanner.model.source.remote.RemoteDataSource;

/**
 * Created by elmira on 4/3/17.
 */

public class Repository implements DataSource {

    private static final String LOG_TAG = "Repository";

    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    private LinkedHashMap<String, Trip> mCachedTrips;
    private static Repository SHARED_INSTANCE;

    private boolean canLoadFromRemoteSource = true;
    private FbUser mCurrentFbUser;

    public static Repository getInstance(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        if (SHARED_INSTANCE == null) {
            SHARED_INSTANCE = new Repository(localDataSource, remoteDataSource);
        }
        return SHARED_INSTANCE;
    }

    private Repository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;

        mCachedTrips = new LinkedHashMap<>();
    }

    @Override
    public void loadOpenTrips(final LoadTripListCallback callback) {
        //load data from local data source
        localDataSource.loadOpenTrips(new LoadTripListCallback() {
            @Override
            public void onTripListLoaded(List<Trip> trips) {
                addTripsToCache(trips);
                callback.onTripListLoaded(trips);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });

        if (canLoadFromRemoteSource) {
            //load data from remote data source
            remoteDataSource.loadOpenTrips(new LoadTripListCallback() {
                @Override
                public void onTripListLoaded(List<Trip> trips) {
                    addTripsToCache(trips);
                    callback.onTripListLoaded(trips);

                    // sync with local data source
                    if (trips != null && trips.size() > 0) {
                        for (Trip trip : trips) {
                            trip.unpinInBackground();
                            trip.pinInBackground();

                            remoteDataSource.loadTripDestinations(trip, new LoadTripCallback() {
                                @Override
                                public void onTripLoaded(Trip trip) {
                                    //save trip to local data source
                                    localDataSource.updateTrip(trip);
                                }

                                @Override
                                public void onFailure() {
                                    //do nothing
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure() {
                    callback.onFailure();
                }
            });
        }
    }

    @Override
    public void loadTrip(final String tripId, final LoadTripCallback callback) {
        Log.d(LOG_TAG, "Loading trip: " + tripId);
        //return Trip immediately if it stores in the cache
        if (mCachedTrips.containsKey(tripId)) {
            Log.d(LOG_TAG, "Found trip in memory cache");
            callback.onTripLoaded(mCachedTrips.get(tripId));
        }
        // try to load trip from local source
        localDataSource.loadTrip(tripId, new LoadTripCallback() {
            @Override
            public void onTripLoaded(final Trip trip) {
                Log.d(LOG_TAG, "Found trip in local database.");
                mCachedTrips.put(tripId, trip);
                callback.onTripLoaded(trip);
            }

            @Override
            public void onFailure() {
                // try to load trip from remote source
                remoteDataSource.loadTrip(tripId, new LoadTripCallback() {
                    @Override
                    public void onTripLoaded(final Trip trip) {
                        Log.d(LOG_TAG, "Found trip in remote source. ");
                        mCachedTrips.put(tripId, trip);
                        callback.onTripLoaded(trip);

                        //sync with local data source
                        localDataSource.updateTrip(trip);
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
    public void updateTrip(final Trip trip, final SaveTripCallback callback) {
        //update memory cache
        mCachedTrips.put(trip.getTripId(), trip);
        //update local database
        localDataSource.updateTrip(trip, new SaveTripCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
                //update remotely
                remoteDataSource.updateTrip(trip);

                //load and save trip photos
                loadAndSaveTripPhotos(trip);
            }

            @Override
            public void onFailed() {
                callback.onFailed();
            }
        });
    }

    @Override
    public void updateTrip(Trip trip) {
        //update memory cache
        mCachedTrips.put(trip.getTripId(), trip);
        //update local database
        localDataSource.updateTrip(trip);
        //update remotely
        remoteDataSource.updateTrip(trip);
    }

    public void loadAndSaveTripPhotos(Trip trip) {
        if (trip == null || trip.getDestinations() == null) return;

        for (final Destination destination : trip.getDestinations()) {
            remoteDataSource.loadPlace(destination.getPointId(), new LoadPlaceCallback() {
                @Override
                public void onPlaceLoaded(Point place) {
                    destination.setPhotoReference(place.getPhotoReference());
                    updateDestination(destination);
                }

                @Override
                public void onFailure() {
                    // do nothing
                }
            });
        }
    }

    @Override
    public void loadTripDestinations(Trip trip, LoadTripCallback callback) {
        localDataSource.loadTripDestinations(trip, callback);
        if (canLoadFromRemoteSource) {
            remoteDataSource.loadTripDestinations(trip, callback);
        }
    }

    public void setCanLoadFromRemoteSource(boolean canLoadFromRemoteSource) {
        this.canLoadFromRemoteSource = canLoadFromRemoteSource;
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

    private void addTripsToCache(List<Trip> list) {
        if (list == null || list.size() == 0) return;

        mCachedTrips.clear();
        for (Trip trip : list) {
            mCachedTrips.put(trip.getTripId(), trip);
        }
    }

    public boolean isCurrentFbUserDefined() {
        Log.d(LOG_TAG, "current FB user: " + (mCurrentFbUser == null ? "undefined" : mCurrentFbUser.toString()));
        return mCurrentFbUser != null;
    }

    @Override
    public void loadPlace(final String placeId, final LoadPlaceCallback callback) {
        localDataSource.loadPlace(placeId, new LoadPlaceCallback() {
            @Override
            public void onPlaceLoaded(Point place) {
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
    public void updateDestination(Destination destination) {
        //update local database
        localDataSource.updateDestination(destination);
        //update remotely
        remoteDataSource.updateDestination(destination);
    }
}
