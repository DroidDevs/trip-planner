package droiddevs.com.tripplanner.triplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.activities.LoginActivity;
import droiddevs.com.tripplanner.addedittrip.AddEditTripActivity;

public class TripListActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = (TextView)
                ButterKnife.findById(toolbar, R.id.toolbar_title);
        tvTitle.setText("Trips");

        // Check for user authentication
        if (AccessToken.getCurrentAccessToken() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

//        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);
//        query.findInBackground(new FindCallback<Trip>() {
//            @Override
//            public void done(List<Trip> results, ParseException e) {
//
//            }
//        });
    }

    public void createTrip(View view){
        Intent intent = new Intent(this, AddEditTripActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_create_trip:
                Intent intent = new Intent(this, AddEditTripActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
