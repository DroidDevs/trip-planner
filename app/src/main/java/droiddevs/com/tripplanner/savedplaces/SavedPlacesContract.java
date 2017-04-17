package droiddevs.com.tripplanner.savedplaces;

import java.util.List;

import droiddevs.com.tripplanner.map.MapContract;
import droiddevs.com.tripplanner.model.map.PlaceItem;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 * Created by elmira on 4/16/17.
 */

public interface SavedPlacesContract {

    interface View extends BaseView<Presenter>{
        void onPlacesLoaded(List<PlaceItem> places);
        void onFailure();
    }

    interface Presenter extends MapContract.Presenter{

    }
}