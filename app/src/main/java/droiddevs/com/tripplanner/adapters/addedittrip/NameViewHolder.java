package droiddevs.com.tripplanner.adapters.addedittrip;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import droiddevs.com.tripplanner.R;

/**
 * Created by elmira on 4/6/17.
 */

public class NameViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tripTitle)
    EditText titleView;

    private OnNameChangeListener mListener;

    public interface OnNameChangeListener {
        void onNameChanged(String name);
    }

    public NameViewHolder(View itemView, OnNameChangeListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;

        titleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mListener != null) {
                    mListener.onNameChanged(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public String getTitle() {
        return titleView.getText().toString();
    }
}
