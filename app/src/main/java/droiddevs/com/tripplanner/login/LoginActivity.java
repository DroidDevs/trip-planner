package droiddevs.com.tripplanner.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import droiddevs.com.tripplanner.R;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.btContinueWithFB) Button btContinueWithFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    // TODO: Error Logging In
                    Log.d("DEBUG", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    // This sets the user permissions so that only this user
                    // can see her created objects
                    ParseACL.setDefaultACL(new ParseACL(), true);

                    // TODO: SUCCESS
                    Log.d("DEBUG", "User signed up and logged in through Facebook!");
                    finish();
                } else {
                    // TODO: SUCCESS
                    Log.d("DEBUG", "User logged in through Facebook!");
                    finish();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
