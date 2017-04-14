package droiddevs.com.tripplanner.tripdetails;

import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.mvp.BasePresenter;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 * Created by Jared12 on 4/9/17.
 */

public class TripDetailsContract {

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);

        void onTripLoaded(Trip trip);

        void onLoadingFailure();

        boolean isActive();
    }

    interface Presenter extends BasePresenter{

    }
}
