package droiddevs.com.tripplanner.tripdestination;

import java.util.List;

import droiddevs.com.tripplanner.model.DestinationOption;
import droiddevs.com.tripplanner.mvp.BasePresenter;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 * Created by Jared12 on 4/13/17.
 */

public interface TripDestinationContract {
    interface View extends BaseView<Presenter> {
        void showDestinationOptions(List<DestinationOption> options);
    }

    interface Presenter extends BasePresenter {
        void loadDestinationOptions();
    }
}
