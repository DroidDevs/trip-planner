package droiddevs.com.tripplanner.addedittrip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;

/**
 * Created by elmira on 4/17/17.
 */

public class SelectDurationFragment extends DialogFragment {

    public static final String ARGUMENT_DESTINATION_ID = "destinationId";
    public static final String LOG_TAG = "SelectDurationFragment";

    @BindView(R.id.tvOneDay)
    TextView oneDayTextView;

    @BindView(R.id.tvTwoDays)
    TextView twoDaysTextView;

    @BindView(R.id.tvThreeDays)
    TextView threeDaysTextView;

    @BindView(R.id.tvOneWeek)
    TextView oneWeekTextView;

    @BindView(R.id.tvTwoWeeks)
    TextView twoWeeks;

    private String destinationId;

    interface OnDurationSelectedListener {
        void onDurationSelected(String destinationId, int durationIdDays);
    }

    public static SelectDurationFragment newInstance(String destinationId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_DESTINATION_ID, destinationId);

        SelectDurationFragment fragment = new SelectDurationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addedit_trip_select_duration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        destinationId = getArguments().getString(ARGUMENT_DESTINATION_ID);
        if (destinationId == null) {
            dismiss();
        }

        oneDayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDurationSelected(1);
            }
        });

        twoDaysTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDurationSelected(2);
            }
        });

        threeDaysTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDurationSelected(3);
            }
        });
        oneWeekTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDurationSelected(7);
            }
        });
        twoDaysTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDurationSelected(14);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout(width, height);
    }

    private void onDurationSelected(int durationInDays) {
        OnDurationSelectedListener listener = (OnDurationSelectedListener) getTargetFragment();

        if (listener != null) {
            listener.onDurationSelected(destinationId, durationInDays);
        }

        dismiss();
    }
}