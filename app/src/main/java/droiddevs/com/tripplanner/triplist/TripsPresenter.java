package droiddevs.com.tripplanner.triplist;

import android.support.annotation.NonNull;

import droiddevs.com.tripplanner.model.source.Repository;

/**
 * Created by jared.manfredi on 4/5/17.
 */

public class TripsPresenter implements TripsContract.Presenter {
    private final Repository mRepository;
    private final TripsContract.View mTripsView;

    public TripsPresenter(@NonNull Repository repository, @NonNull TripsContract.View tripsView) {
        mRepository = repository;
        mTripsView = tripsView;
    }

    @Override
    public void start() {
        loadTrips();
    }

    @Override
    public void loadTrips() {
        //mRepository.getTrips()
    }
}
