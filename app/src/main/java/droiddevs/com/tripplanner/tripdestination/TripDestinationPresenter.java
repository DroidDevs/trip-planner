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
                        ContextCompat.getDrawable(mContext, R.drawable.ic_saved_places),
                        PlaceOption.PlaceOptionType.TYPE_SAVED_PLACES));

        tempOptions.add(
                PlaceOption.newInstance(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_cappuccino),
                        PlaceOption.PlaceOptionType.TYPE_CAFE));

        tempOptions.add(
                PlaceOption.newInstance(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_pizza),
                        PlaceOption.PlaceOptionType.TYPE_RESTAURANT));

        tempOptions.add(
                PlaceOption.newInstance(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_drinks),
                        PlaceOption.PlaceOptionType.TYPE_DRINK));

        mDestinationOptionsView.showDestinationPlaceOptions(tempOptions);
    }
}
