package droiddevs.com.tripplanner.tripdestination;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.PlaceOption;

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
        loadDestinationPlaceOptions();
    }

    @Override
    public void loadDestinationPlaceOptions() {
        List<PlaceOption> tempOptions = new ArrayList<>();
        tempOptions.add(
                PlaceOption.newInstance(
                        ContextCompat.getDrawable(mContext, R.drawable.cafe),
                        PlaceOption.PlaceOptionType.TYPE_CAFE));
        tempOptions.add(
                PlaceOption.newInstance(
                        ContextCompat.getDrawable(mContext, R.drawable.restaurant),
                        PlaceOption.PlaceOptionType.TYPE_RESTAURANT));
        tempOptions.add(
                PlaceOption.newInstance(
                        ContextCompat.getDrawable(mContext, R.drawable.drinks),
                        PlaceOption.PlaceOptionType.TYPE_DRINK));
        tempOptions.add(
                PlaceOption.newInstance(
                        ContextCompat.getDrawable(mContext, R.drawable.savedplaces),
                        PlaceOption.PlaceOptionType.TYPE_SAVED_PLACES));
        mDestinationOptionsView.showDestinationPlaceOptions(tempOptions);
    }
}
