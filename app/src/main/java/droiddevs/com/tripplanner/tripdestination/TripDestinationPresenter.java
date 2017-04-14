package droiddevs.com.tripplanner.tripdestination;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.DestinationOption;

/**
 * Created by Jared12 on 4/13/17.
 */

public class TripDestinationPresenter implements TripDestinationContract.Presenter {
    private final TripDestinationContract.View mDestinationOptionsView;
    private Context mContext;

    public TripDestinationPresenter(Context context, @NonNull TripDestinationContract.View destinationView) {
        mContext = context;
        mDestinationOptionsView = destinationView;
        mDestinationOptionsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadDestinationOptions();
    }

    @Override
    public void loadDestinationOptions() {
        List<DestinationOption> tempOptions = new ArrayList<>();
        tempOptions.add(DestinationOption.newInstance(ContextCompat.getDrawable(mContext, R.drawable.cafe), "Cafe"));
        tempOptions.add(DestinationOption.newInstance(ContextCompat.getDrawable(mContext, R.drawable.restaurant), "Restaurants"));
        tempOptions.add(DestinationOption.newInstance(ContextCompat.getDrawable(mContext, R.drawable.drinks), "Drinks"));
        tempOptions.add(DestinationOption.newInstance(ContextCompat.getDrawable(mContext, R.drawable.savedplaces), "Saved Places"));
        mDestinationOptionsView.showDestinationOptions(tempOptions);
    }
}
