package droiddevs.com.tripplanner.triplist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jared.manfredi on 4/5/17.
 */

public class TripsFragment extends Fragment implements TripsContract.View {
    TripsContract.Presenter mPresenter;

    public static TripsFragment newInstance() {
        return new TripsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void setPresenter(TripsContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
