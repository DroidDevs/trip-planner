package droiddevs.com.tripplanner.model.source;

import java.util.ArrayList;
import java.util.List;

import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.local.LocalDataSource;
import droiddevs.com.tripplanner.model.source.remote.RemoteDataSource;

/**
 * Created by elmira on 4/3/17.
 */

public class Repository implements DataSource {

    private static Repository SHARED_INSTANCE;

    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    public Repository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public Repository() {

    }

    public static Repository getInstance() {
        if (SHARED_INSTANCE == null) {
            SHARED_INSTANCE = new Repository();
        }
        return SHARED_INSTANCE;
    }

    public List<Trip> getTrips() {
        return new ArrayList<Trip>();
    }
}
