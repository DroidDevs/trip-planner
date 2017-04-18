package droiddevs.com.tripplanner.suggestedplacedetails;

import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.mvp.BasePresenter;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 * Created by Jared12 on 4/15/17.
 */

public class SuggestedPlaceDetailsContract {
    public interface View extends BaseView<Presenter> {
        void showPlaceDetails(GooglePlace place, boolean savedPlace);

        void onPlaceSaved(boolean success);

        void onPlaceDeleted(boolean success);
    }

    public interface Presenter extends BasePresenter {
        void savePlace(GooglePlace place);

        void deletePlace(GooglePlace place);
    }
}