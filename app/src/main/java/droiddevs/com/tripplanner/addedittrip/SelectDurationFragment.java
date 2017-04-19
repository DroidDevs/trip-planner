package droiddevs.com.tripplanner.addedittrip;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
    TextView twoWeeksTextView;

    @BindView(R.id.tvPickDays)
    TextView pickDaysTextView;

    @BindView(R.id.etDuration)
    EditText durationEditText;

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
        Window window = getDialog().getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }
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
        twoWeeksTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDurationSelected(14);
            }
        });
        pickDaysTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDaysTextView.setVisibility(View.GONE);
                durationEditText.setVisibility(View.VISIBLE);

                durationEditText.setFocusable(true);
                durationEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
                durationEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
            }
        });
        durationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    int value = Integer.parseInt(v.getText().toString());
                    onDurationSelected(value);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(width, height);
        }
    }

    private void onDurationSelected(int durationInDays) {
        OnDurationSelectedListener listener = (OnDurationSelectedListener) getTargetFragment();

        if (listener != null) {
            listener.onDurationSelected(destinationId, durationInDays);
        }

        dismiss();
    }
}