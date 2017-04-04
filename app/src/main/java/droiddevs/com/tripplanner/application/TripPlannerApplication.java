package droiddevs.com.tripplanner.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

import droiddevs.com.tripplanner.model.Trip;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Jared12 on 4/4/17.
 */

public class TripPlannerApplication extends Application {
    @Override
    public void onCreate() {
        Fabric.with(this, new Crashlytics());
        super.onCreate();

        // allow Parse to store data in local database
        Parse.enableLocalDatastore(this);
        //register all model types
        ParseObject.registerSubclass(Trip.class);
        //initialize Parse
        Parse.initialize(this);
        // allow Parse login integration with facebook
        ParseFacebookUtils.initialize(this);

        //initialize Stetho
        Stetho.initializeWithDefaults(this);
    }
}