package droiddevs.com.tripplanner.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Point;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.Repository;
import droiddevs.com.tripplanner.model.source.local.LocalDataSource;
import droiddevs.com.tripplanner.model.source.remote.RemoteDataSource;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Jared12 on 4/4/17.
 */

public class TripPlannerApplication extends Application {

    private static Repository repository;

    @Override
    public void onCreate() {
        Fabric.with(this, new Crashlytics());
        super.onCreate();

        // allow Parse to store data in local database
        Parse.enableLocalDatastore(this);

        // allow Parse login integration with facebook
        Parse.initialize(this);
        ParseFacebookUtils.initialize(this);

        // set default acl
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);

        //register all model types
        ParseObject.registerSubclass(Trip.class);
        ParseObject.registerSubclass(Destination.class);
        ParseObject.registerSubclass(Point.class);

        //initialize Stetho
        Stetho.initializeWithDefaults(this);

        repository = Repository.getInstance(LocalDataSource.getInstance(this), RemoteDataSource.getInstance(this));
    }

    public static Repository getRepository() {
        return repository;
    }
}