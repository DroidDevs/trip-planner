package droiddevs.com.tripplanner.suggestedplacedetails;

import droiddevs.com.tripplanner.model.googleplaces.GooglePlace;
import droiddevs.com.tripplanner.model.source.Repository;

/**
 * Created by Jared12 on 4/15/17.
 */

public class SuggestedPlaceDetailsPresenter implements SuggestedPlaceDetailsContract.Presenter {
    private GooglePlace mCurrentPlace;

    private SuggestedPlaceDetailsContract.View mView;
    private Repository mRepository;

    public SuggestedPlaceDetailsPresenter(Repository repository, SuggestedPlaceDetailsContract.View view, GooglePlace currentPlace) {
        mCurrentPlace = currentPlace;

        mView = view;
        mRepository = repository;

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.showPlaceDetails(mCurrentPlace);
    }
}
