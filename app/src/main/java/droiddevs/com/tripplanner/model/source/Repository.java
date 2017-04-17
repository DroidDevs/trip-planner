package droiddevs.com.tripplanner.model.source;

import android.location.Location;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.List;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.fb.FbUser;
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
    private List<FbUser> mFbFriends;

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
                    //callback.onFailure();
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
            remoteDataSource.loadPlace(destination.getPlaceId(), new LoadPlaceCallback() {
                @Override
                public void onPlaceLoaded(SavedPlace place) {
                    if (place.getPhotoReference()!=null) {
                        destination.setPhotoReference(place.getPhotoReference());
                        updateDestination(destination);
                    }
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

    @Override
    public void loadDestination(final String destinationId, final LoadDestinationCallback callback) {
        localDataSource.loadDestination(destinationId, new LoadDestinationCallback() {
            @Override
            public void onDestinationLoaded(Destination destination) {
                callback.onDestinationLoaded(destination);
            }

            @Override
            public void onFailure() {
                if (canLoadFromRemoteSource) {
                    remoteDataSource.loadDestination(destinationId, callback);
                    return;
                }
                callback.onFailure();
            }
        });
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

    @Override
    public void searchFbPlaces(Location location, int radiusInMeters, int resultsLimit, String searchText, SearchFbPlacesCallback callback) {
        remoteDataSource.searchFbPlaces(location, radiusInMeters, resultsLimit, searchText, callback);
    }

    @Override
    public void searchGooglePlaces(String location, int radiusInMeters, String searchText, String apiKey, SearchGooglePlacesCallback callback) {
        remoteDataSource.searchGooglePlaces(location, radiusInMeters, searchText, apiKey, callback);
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

    public boolean isFbFriendsLoaded() {
        Log.d(LOG_TAG, "FB friends list: " + (mFbFriends == null ? "undefined" : mFbFriends.size()));
        return mFbFriends != null;
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
    public void updateDestination(Destination destination) {
        //update local database
        localDataSource.updateDestination(destination);
        //update remotely
        remoteDataSource.updateDestination(destination);
    }

    @Override
    public void deleteTrip(final Trip trip, final DeleteTripCallback callback) {
        localDataSource.deleteTrip(trip, new DeleteTripCallback() {
            @Override
            public void onTripDeleted() {
                remoteDataSource.deleteTrip(trip, callback);
            }
        });
    }

    @Override
    public void loadSavedPlaces(String destinationId, final LoadSavedPlacesCallback callback) {
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
        if (canLoadFromRemoteSource) {
            //load data from remote data source
            remoteDataSource.loadSavedPlaces(destinationId, new LoadSavedPlacesCallback() {
                @Override
                public void onSavedPlacesLoaded(List<SavedPlace> places) {
                    callback.onSavedPlacesLoaded(places);
                    if (places != null && places.size() > 0) {
                        for (SavedPlace place : places) {
                            place.unpinInBackground();
                            place.pinInBackground();
                        }
                    }
                }

                @Override
                public void onFailure() {
                    // do nothing
                }
            });
        }
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
}
