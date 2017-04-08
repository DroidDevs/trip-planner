package droiddevs.com.tripplanner.adapters.addedittrip;

import android.app.DatePickerDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;

/**
 * Created by elmira on 4/6/17.
 */

public class StartDateViewHolder extends RecyclerView.ViewHolder implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.tvStartDate)
    TextView mStartDateTextView;

    private DatePickerDialog mDatePickerDialog;

    private Calendar mCalendar = Calendar.getInstance();
    private Date mStartDate;

    private static SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
    private OnStartDateChangeListener mListener;

    public interface OnStartDateChangeListener {
        void onStartDateChanged(Date startDate);
    }

    public StartDateViewHolder(final View itemView, OnStartDateChangeListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;

        mStartDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog = new DatePickerDialog(itemView.getContext(), StartDateViewHolder.this, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        mStartDate = mCalendar.getTime();
        mStartDateTextView.setText(sdf.format(mStartDate));

        if (mListener != null) {
            mListener.onStartDateChanged(mStartDate);
        }
    }

    public void setStartDate(Date startDate) {
        if (startDate == null) return;
        mStartDate = startDate;
        mCalendar.setTime(startDate);
        mStartDateTextView.setText(sdf.format(startDate));
    }
}