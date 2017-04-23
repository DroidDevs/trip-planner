package droiddevs.com.tripplanner.map;

import android.content.Context;

import java.util.List;

import droiddevs.com.tripplanner.mvp.BasePresenter;
import droiddevs.com.tripplanner.mvp.BaseView;

/**
 * Created by elmira on 4/11/17.
 */

public interface MapContract {

    interface View<T> extends BaseView<Presenter>{

        void setMapData(List<T> data);

        void onLoadFailure();

        boolean isActive();

        Context getContext();
    }

    interface Presenter extends BasePresenter{
        void reloadData();
    }
}
