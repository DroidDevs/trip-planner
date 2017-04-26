package droiddevs.com.tripplanner.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;

import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.model.source.Repository;

/**
 * Created by elmira on 4/12/17.
 */

public class OauthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticate();
    }

    public void authenticate() {
        if (AccessToken.getCurrentAccessToken() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Repository repository = TripPlannerApplication.getRepository();
            if (!repository.isCurrentFbUserDefined()) {
                repository.loadCurrentFBUser(this, null);
            }
        }
    }
}