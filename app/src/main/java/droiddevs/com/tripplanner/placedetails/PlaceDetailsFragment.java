package droiddevs.com.tripplanner.placedetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

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
    @BindView(R.id.btnSavePlace)
    Button btnSavePlace;
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

//        OpeningHours openingHours = place.getOpeningHours();
//        if (openingHours != null) {
//            if (openingHours.getOpenNow()) {
//                tvOpenNow.setText("Open Now");
//            }
//            else {
//                tvOpenNow.setText("Closed");
//            }
//        }

        setAsSaved(savedPlace);
        btnSavePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedPlace) {
                    //mPresenter.deletePlace(place);
                }
                else {
                    //mPresenter.savePlace(place);
                }
            }
        });
    }

    @Override
    public void onPlaceSaved(boolean success) {
        setAsSaved(success);
    }

    @Override
    public void onPlaceDeleted(boolean success) {
        setAsSaved(!success);
    }

    private void setAsSaved(boolean saved) {
        if (saved) {
            btnSavePlace.setText("Remove");
            btnSavePlace.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }
        else {
            btnSavePlace.setText("Save");
            btnSavePlace.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
        }
    }

}