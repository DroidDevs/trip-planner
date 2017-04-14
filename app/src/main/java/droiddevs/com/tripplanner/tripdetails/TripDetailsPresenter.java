package droiddevs.com.tripplanner.tripdetails;

import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.model.source.DataSource;
import droiddevs.com.tripplanner.model.source.Repository;

/**
 * Created by Jared12 on 4/9/17.
 */

public class TripDetailsPresenter implements TripDetailsContract.Presenter {

    private TripDetailsContract.View mView;
    private Repository mRepository;
    private String mTripId;

    public TripDetailsPresenter(TripDetailsContract.View mView, Repository mRepository, String tripId) {
        this.mView = mView;
        this.mRepository = mRepository;
        this.mView.setPresenter(this);
        this.mTripId = tripId;
    }

    @Override
    public void start() {
        mView.setLoadingIndicator(true);
        mRepository.loadTrip(mTripId, new DataSource.LoadTripCallback() {
            @Override
            public void onTripLoaded(Trip trip) {
                if (mView != null && mView.isActive()) {
                    mView.setLoadingIndicator(false);
                    mView.onTripLoaded(trip);
                }
            }

            @Override
            public void onFailure() {
                if (mView != null && mView.isActive()) {
                    mView.setLoadingIndicator(false);
                    mView.onLoadingFailure();
                }
            }
        });
    }
}
