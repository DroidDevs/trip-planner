package droiddevs.com.tripplanner.tripdetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import droiddevs.com.tripplanner.R;

public class TripDetailsActivity extends AppCompatActivity {
    @BindView(R.id.include_toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        // Setup toolbar
        setSupportActionBar(toolbar);
    }
}
