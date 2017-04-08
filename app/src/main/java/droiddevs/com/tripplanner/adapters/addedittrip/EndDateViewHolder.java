package droiddevs.com.tripplanner.adapters.addedittrip;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;

/**
 * Created by elmira on 4/7/17.
 */

public class EndDateViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvEndDate)
    TextView mEndDateTextView;

    private static SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");

    public EndDateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setEndDate(Date endDate) {
        if (endDate == null) return;
        mEndDateTextView.setText(sdf.format(endDate));
    }
}