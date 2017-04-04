package droiddevs.com.tripplanner.model.source;

import droiddevs.com.tripplanner.model.source.local.LocalDataSource;
import droiddevs.com.tripplanner.model.source.remote.RemoteDataSource;

/**
 * Created by elmira on 4/3/17.
 */

public class Repository implements DataSource {

    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    public Repository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }
}
