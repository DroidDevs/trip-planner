package droiddevs.com.tripplanner.suggestedplacedetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.model.googleplaces.OpeningHours;

public class SuggestedPlaceDetailsFragment extends Fragment implements SuggestedPlaceDetailsContract.View {
    @BindView(R.id.tvPlaceTitle)
    TextView tvPlaceTitle;
    @BindView(R.id.rbPlaceRating)
    RatingBar rbPlaceRating;
    @BindView(R.id.btnSavePlace)
    Button btnSavePlace;
    @BindView(R.id.tvOpenNow)
    TextView tvOpenNow;

    private Unbinder mUnbinder;
    private SuggestedPlaceDetailsContract.Presenter mPresenter;

    public SuggestedPlaceDetailsFragment() {}

    public static SuggestedPlaceDetailsFragment newInstance() {
        SuggestedPlaceDetailsFragment fragment = new SuggestedPlaceDetailsFragment();
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
    public void setPresenter(SuggestedPlaceDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPlaceDetails(GooglePlace place) {
        tvPlaceTitle.setText(place.getName());
        rbPlaceRating.setRating(place.getRating());

        OpeningHours openingHours = place.getOpeningHours();
        if (openingHours != null) {
            if (openingHours.getOpenNow()) {
                tvOpenNow.setText("Open Now");
            } else {
                tvOpenNow.setText("Closed");
            }
        }

        btnSavePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Save Place
            }
        });
    }
}
