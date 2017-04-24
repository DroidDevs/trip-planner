package droiddevs.com.tripplanner.placedetails;

import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.mvp.BasePresenter;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 *
 * Created by Jared12 on 4/15/17.
 * Updated by Elmira Andreeva on 4/24/2017
 */

public class PlaceDetailsContract {

    public interface View extends BaseView<Presenter> {

        void showPlaceDetails(PlaceItem place);

        void onFailure();

        boolean isActive();
    }

    public interface Presenter extends BasePresenter {
        void savePlace();

        void deletePlace();

        PlaceItem getCurrentPlace();
    }
}