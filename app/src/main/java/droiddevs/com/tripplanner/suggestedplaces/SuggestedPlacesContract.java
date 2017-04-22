package droiddevs.com.tripplanner.suggestedplaces;

import java.util.List;
import java.util.Set;

import droiddevs.com.tripplanner.map.MapContract;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 * Created by Jared12 on 4/15/17.
 */

public class SuggestedPlacesContract {

    interface View extends BaseView<Presenter> {

        void showSuggestedPlaces(List<PlaceItem> places, Set<String> savedPlaceIds);

        void onSavedPlaceDeleted(PlaceItem placeItem);
    }

    interface Presenter extends MapContract.Presenter {
        void loadSuggestedPlaces(String placeTypeSearchString, String destinationPointId);

        void savePlace(PlaceItem placeItem);

        void deletePlace(PlaceItem placeItem);
    }
}
