package droiddevs.com.tripplanner.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.application.TripPlannerApplication;
import droiddevs.com.tripplanner.model.fb.FbUser;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;
import droiddevs.com.tripplanner.triplist.TripsActivity;
import droiddevs.com.tripplanner.util.AlertUtil;

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
        final List<String> permissions = Arrays.asList("email");
        final AlertUtil alertUtil = new AlertUtil(this);
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    alertUtil.showAlert("Log in failed", "Log in with Facebook failed, please try again.");
                } else {
                    if (user.isNew()) { // New user
                        saveUserInfoFromFacebook(); // Save email to parse
                    }
                    showTrips();
                }
            }
        });
    }

    private void saveUserInfoFromFacebook() {
        Repository repo = TripPlannerApplication.getRepository();
        repo.loadCurrentFBUser(new DataSource.LoadFbUserCallback() {
            @Override
            public void onUserLoaded(FbUser user) throws ParseException {
                if (user.getEmail() != null) {
                    ParseUser parseUser = ParseUser.getCurrentUser();
                    if (parseUser != null) {
                        parseUser.setEmail(user.getEmail());
                        parseUser.save();
                    }
                }
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private void showTrips() {
        Intent intent = new Intent(LoginActivity.this, TripsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
