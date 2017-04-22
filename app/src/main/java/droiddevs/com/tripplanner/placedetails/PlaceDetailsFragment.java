package droiddevs.com.tripplanner.placedetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.map.PlaceItem;

public class PlaceDetailsFragment extends Fragment implements PlaceDetailsContract.View {
    @BindView(R.id.tvPlaceTitle)
    TextView tvPlaceTitle;
    @BindView(R.id.rbPlaceRating)
    RatingBar rbPlaceRating;
    @BindView(R.id.tbFavorite)
    ToggleButton tbFavorite;
    @BindView(R.id.tvOpenNow)
    TextView tvOpenNow;

    private Unbinder mUnbinder;
    private PlaceDetailsContract.Presenter mPresenter;

    public PlaceDetailsFragment() {
    }

    public static PlaceDetailsFragment newInstance() {
        PlaceDetailsFragment fragment = new PlaceDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_suggested_place_details, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void setPresenter(PlaceDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPlaceDetails(final PlaceItem place, final boolean savedPlace) {
        tvPlaceTitle.setText(place.getName());
        rbPlaceRating.setRating(place.getRating());

        List<Object> weekdayHours = place.getWeekdayHours();
        if (weekdayHours != null && weekdayHours.size() > 0) {

        }

        tbFavorite.setChecked(savedPlace);
        tbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (savedPlace) {
                    mPresenter.deletePlace(place);
                } else {
                    mPresenter.savePlace(place);
                }
            }
        });
    }
}
