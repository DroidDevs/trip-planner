package droiddevs.com.tripplanner.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Jared12 on 4/4/17.
 */

public class TripPlannerApplication extends Application {
    @Override
    public void onCreate() {
        Fabric.with(this, new Crashlytics());
        super.onCreate();

        Parse.initialize(this);
        ParseFacebookUtils.initialize(this);
    }
}
