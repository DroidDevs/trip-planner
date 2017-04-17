package droiddevs.com.tripplanner.suggestedplacedetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import droiddevs.com.tripplanner.R;

public class SuggestedPlaceDetailsFragment extends Fragment implements SuggestedPlaceDetailsContract.View {

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggested_place_details, container, false);
    }

    @Override
    public void setPresenter(SuggestedPlaceDetailsContract.Presenter presenter) {

    }
}
