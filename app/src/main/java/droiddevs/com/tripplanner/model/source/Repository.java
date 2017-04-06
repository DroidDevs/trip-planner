package droiddevs.com.tripplanner.model.source;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.LinkedHashMap;
import java.util.List;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.local.LocalDataSource;
import droiddevs.com.tripplanner.model.source.remote.RemoteDataSource;

/**
 * Created by elmira on 4/3/17.
 */

public class Repository implements DataSource {

    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    private LinkedHashMap<String, Trip> mCachedTrips;
    private static Repository instance;

    private Repository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;

        mCachedTrips = new LinkedHashMap<>();
    }

    public static Repository getInstance(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        if (instance == null) {
            instance = new Repository(localDataSource, remoteDataSource);
        }
        return instance;
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

        //load data from remote data source
        remoteDataSource.loadOpenTrips(new LoadTripListCallback() {
            @Override
            public void onTripListLoaded(List<Trip> trips) {
                addTripsToCache(trips);
                callback.onTripListLoaded(trips);

                //todo sync with local data source
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    @Override
    public void loadTrip(final String tripId, final LoadTripCallback callback) {
        //return Trip immediately if it stores in the cache
        if (mCachedTrips.containsKey(tripId)) {
            callback.onTripLoaded(mCachedTrips.get(tripId));
        }
        // try to load trip from local source
        localDataSource.loadTrip(tripId, new LoadTripCallback() {
            @Override
            public void onTripLoaded(final Trip trip) {
                ParseObject.fetchAllInBackground(trip.getDestinations(), new FindCallback<Destination>() {
                    @Override
                    public void done(List<Destination> objects, ParseException e) {
                        if (objects != null) {
                            trip.setDestinations(objects);
                            mCachedTrips.put(tripId, trip);
                            callback.onTripLoaded(trip);
                        }
                    }
                });
            }

            @Override
            public void onFailure() {
                //do nothing
            }
        });
        // try to load trip from remote source
        remoteDataSource.loadTrip(tripId, new LoadTripCallback() {
            @Override
            public void onTripLoaded(final Trip trip) {
                ParseObject.fetchAllInBackground(trip.getDestinations(), new FindCallback<Destination>() {
                    @Override
                    public void done(List<Destination> objects, ParseException e) {
                        if (objects != null) {
                            trip.setDestinations(objects);
                            mCachedTrips.put(tripId, trip);
                            callback.onTripLoaded(trip);

                            //todo sync with local data source
                        }
                    }
                });
            }

            @Override
            public void onFailure() {
                //callback.onFailure();
            }
        });
    }

    @Override
    public void updateTrip(Trip trip, SaveTripCallback callback) {
        //update memory cache
        mCachedTrips.put(trip.getTripId(), trip);
        //update local database
        localDataSource.updateTrip(trip, callback);
        //update remotely
        remoteDataSource.updateTrip(trip, callback);
    }

    private void addTripsToCache(List<Trip> list) {
        if (list == null || list.size() == 0) return;

        mCachedTrips.clear();
        for (Trip trip : list) {
            mCachedTrips.put(trip.getTripId(), trip);
        }
    }
}
