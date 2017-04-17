package droiddevs.com.tripplanner.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.triplist.TripsActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.btContinueWithFB)
    Button btContinueWithFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Login OnClick
        btContinueWithFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
    }

    public void onLogin() {
        // Facebook permissions requested
        // TODO: We need to add permissions we want here
        final List<String> permissions = Arrays.asList("email");
        //final List<String> permissions = Arrays.asList("email", "user_hometown", "user_location");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    // TODO: Error Logging In
                    Log.d("DEBUG", "Uh oh. The user cancelled the Facebook login.");
                }
                else {
                    if (user.isNew()) { // New user
                        saveUserInfoFromFacebook(); // Save email to parse
                    }
                    showTrips();
                }
            }
        });
    }

    // TODO: SHOULD MOVE THIS GRAPH REQUEST TO REMOTE DATASOURCE
    private void saveUserInfoFromFacebook() {
        Bundle params = new Bundle();
        params.putString("fields", "first_name,last_name,email");

        GraphRequest request = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), "me", new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {

                try {
                    JSONObject responseObject = response.getJSONObject();
                    if (responseObject.has("email")) {
                        ParseUser user = ParseUser.getCurrentUser();
                        if (user != null) {
                            String userEmail = responseObject.getString("email");
                            user.setEmail(userEmail);
                            user.save();
                        }
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "Error deserializing json");
                } catch (ParseException e) {
                    Log.d(TAG, "Error saving parse user email");
                }
            }
        });

        request.setParameters(params);
        request.executeAsync();
    }

    private void showTrips() {
        Intent intent = new Intent(LoginActivity.this, TripsActivity.class);
        startActivity(intent);
        finish();
    }

    private void setACL() {
        ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
        acl.setPublicReadAccess(false);
        ParseACL.setDefaultACL(acl, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
