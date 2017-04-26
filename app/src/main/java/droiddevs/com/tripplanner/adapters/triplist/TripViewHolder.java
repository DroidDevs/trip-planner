package droiddevs.com.tripplanner.adapters.triplist;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Trip;
import droiddevs.com.tripplanner.util.PhotoUrlUtil;

/**
 * Created by Jared12 on 4/9/17.
 * <p>
 * Updated by Elmira Andreeva on 4/25/17:
 * 1) added back card view with static map of trip destinations
 * 2) implemented cardFlip animation
 */

public class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String LOG_TAG = "TripViewHolder";

    public interface TripViewHolderListener {
        void OnTripClicked(int position);
    }

    @BindView(R.id.tvTripTitle)
    TextView tvTripTitle;
    @BindView(R.id.tvTripDates)
    TextView tvTripDate;
    @BindView(R.id.ivTripImage)
    ImageView ivTripImage;
    @BindView(R.id.ibTripMenu)
    ImageButton ibTripMenu;
    @BindView(R.id.cvTrip)
    View frontCard;
    @BindView(R.id.cvTripBack)
    View backCard;
    @BindView(R.id.ivTripMapImage)
    ImageView tripMapImage;
    @BindView(R.id.ibTripFlip)
    ImageButton flipCardFront;
    @BindView(R.id.ibTripFlipBack)
    ImageButton flipCardBack;

    private TripViewHolderListener mListener;

    final float scale = 100000;

    Animator leftInAnim = null;
    Animator rightOutAnim = null;

    public TripViewHolder(View itemView, TripViewHolderListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        itemView.setOnClickListener(this);

        leftInAnim = AnimatorInflater.loadAnimator(itemView.getContext(), R.animator.card_flip_left_in);
        rightOutAnim = AnimatorInflater.loadAnimator(itemView.getContext(), R.animator.card_flip_right_out);

        changeCameraDistance();
    }

    public void bind(Trip trip) {
        setupFlipCardBack();
        setupFlipCardFront();

        tvTripTitle.setText(trip.getName());

        String dateString = DateUtils.formatDateRange(tvTripTitle.getContext(), trip.getStartDate().getTime(), trip.getEndDate().getTime(), DateUtils.FORMAT_ABBREV_MONTH);
        tvTripDate.setText(dateString);

        String photoUrl = trip.getPhotoUrl();
        if (photoUrl != null) {
            Glide.with(tvTripTitle.getContext())
                    .load(photoUrl)
                    .centerCrop()
                    .into(ivTripImage);
        }

        // bind the back card
        String mapPhotoUrl = PhotoUrlUtil.getTripMapPhotoUrl(trip);
        Log.d(LOG_TAG, "mapPhotoUrl: " + mapPhotoUrl);

        if (mapPhotoUrl != null) {
            Glide.with(tvTripTitle.getContext())
                    .load(mapPhotoUrl)
                    .centerCrop()
                    .into(tripMapImage);
        }
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            if (mListener != null) {
                mListener.OnTripClicked(position);
            }
        }
    }

    private void setupFlipCardFront() {
        flipCardFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightOutAnim.setTarget(frontCard);
                leftInAnim.setTarget(backCard);

                rightOutAnim.start();
                leftInAnim.start();
            }
        });
    }

    private void setupFlipCardBack() {
        flipCardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightOutAnim.setTarget(backCard);
                leftInAnim.setTarget(frontCard);

                rightOutAnim.start();
                leftInAnim.start();
            }
        });
    }

    private void changeCameraDistance() {
        frontCard.setCameraDistance(scale);
        backCard.setCameraDistance(scale);
    }
}
