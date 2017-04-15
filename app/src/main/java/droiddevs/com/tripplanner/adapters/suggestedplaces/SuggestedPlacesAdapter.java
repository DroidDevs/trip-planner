package droiddevs.com.tripplanner.adapters.suggestedplaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.FbPlace;

/**
 * Created by Jared12 on 4/15/17.
 */

public class SuggestedPlacesAdapter extends RecyclerView.Adapter<SuggestedPlacesViewHolder> implements SuggestedPlacesViewHolder.SuggestedPlacesViewHolderListener {
    public interface SuggestedPlaceInteractionListener {
        void OnPlaceClicked(FbPlace place);
    }

    private List<FbPlace> mFbPlaces;
    private Context mContext;

    private SuggestedPlaceInteractionListener mPlaceInteractionListener;

    public SuggestedPlacesAdapter(Context context, List<FbPlace> places) {
        mContext = context;
        mFbPlaces = places;
    }

    public void setSuggestedPlaceInteractionListener(SuggestedPlaceInteractionListener listener) {
        mPlaceInteractionListener = listener;
    }

    @Override
    public SuggestedPlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_suggested_place, parent, false);

        return new SuggestedPlacesViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(SuggestedPlacesViewHolder holder, int position) {
        FbPlace fbPlace = mFbPlaces.get(position);
        holder.tvPlaceTitle.setText(fbPlace.getName());
    }

    @Override
    public int getItemCount() {
        return mFbPlaces.size();
    }

    public void setPlaces(List<FbPlace> places) {
        mFbPlaces.clear();
        if (places != null
                && places.size() > 0) {
            mFbPlaces.addAll(places);
        }
        notifyDataSetChanged();
    }

    @Override
    public void OnPlaceClicked(int position) {
        FbPlace place = mFbPlaces.get(position);
        if (place != null) {
            mPlaceInteractionListener.OnPlaceClicked(place);
        }
    }
}
