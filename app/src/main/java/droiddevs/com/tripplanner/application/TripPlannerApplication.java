package droiddevs.com.tripplanner.application;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.SavedPlace;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.Repository;
import droiddevs.com.tripplanner.model.source.local.LocalDataSource;
import droiddevs.com.tripplanner.model.source.remote.RemoteDataSource;
import droiddevs.com.tripplanner.model.source.remote.RetrofitGooglePlacesService;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Jared12 on 4/4/17.
 */

public class TripPlannerApplication extends Application {

    private static Repository repository;
    private static Context mContext;

    @Override
    public void onCreate() {
        Fabric.with(this, new Crashlytics());
        super.onCreate();

        mContext = this;

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
        ParseObject.registerSubclass(SavedPlace.class);

        //initialize Stetho
        Stetho.initializeWithDefaults(this);

        repository = Repository.getInstance(LocalDataSource.getInstance(this), RemoteDataSource.getInstance(this, RetrofitGooglePlacesService.newGooglePlacesService()));
    }

    public static Repository getRepository() {
        return repository;
    }

    public static String getGooglePlacesApiKey(){
        if (mContext==null) return null;
        return mContext.getString(R.string.google_places_api_key);
    }
}