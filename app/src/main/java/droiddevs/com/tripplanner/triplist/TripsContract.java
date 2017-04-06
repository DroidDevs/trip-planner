package droiddevs.com.tripplanner.triplist;

import droiddevs.com.tripplanner.mvp.BasePresenter;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 * Created by jared.manfredi on 4/5/17.
 */

public interface TripsContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void loadTrips();
    }
}