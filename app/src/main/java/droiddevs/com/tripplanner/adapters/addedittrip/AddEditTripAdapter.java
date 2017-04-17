package droiddevs.com.tripplanner.adapters.addedittrip;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import droiddevs.com.tripplanner.R;
import droiddevs.com.tripplanner.model.Destination;
import droiddevs.com.tripplanner.model.Trip;

/**
 * Created by elmira on 4/6/17.
 */

public class AddEditTripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements NameViewHolder.OnNameChangeListener, StartDateViewHolder.OnStartDateChangeListener {

    private List<Destination> mDestinations;

    private static final int NAME_POSITION = 0;
    private static final int START_DATE_POSITION = 1;
    private static final int END_DATE_POSITION = 2;
    private static final int ITEMS_OFFSET = 3;

    private static final int NAME_ITEM_TYPE = 0;
    private static final int START_DATE_ITEM_TYPE = 1;
    private static final int END_DATE_ITEM_TYPE = 2;
    private static final int DESTINATION_ITEM_TYPE = 3;
    private static final int ADD_ITEM_TYPE = 4;

    private String mName;
    private Date mStartDate;
    private Date mEndDate;

    private OnAddDestinationListener destinationAddListener;
    private OnChangeDestinationListener destinationChangeListener;
    private OnDeleteDestinationListener destinationDeleteListener;
    private StartDateViewHolder.OnStartDateChangeListener startDateChangeListener;
    private OnSelectDurationListener onSelectDurationListener;

    public interface OnChangeDestinationListener {
        void onChangeDestination(String destId);
    }

    public interface OnAddDestinationListener {
        void onAddDestination();
    }

    public interface OnDeleteDestinationListener {
        void onDeleteDestination(String destId);
    }

    public interface OnSelectDurationListener {
        void onSelectDuration(String destId);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NAME_ITEM_TYPE) {
            return new NameViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_addedit_trip_title, parent, false), this);
        }
        else if (viewType == START_DATE_ITEM_TYPE) {
            return new StartDateViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_addedit_trip_start_date, parent, false), this);
        }
        else if (viewType == END_DATE_ITEM_TYPE) {
            return new EndDateViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_addedit_trip_end_date, parent, false));
        }
        else if (viewType == DESTINATION_ITEM_TYPE) {
            return new DestinationViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_addedit_trip_destination, parent, false));
        }
        else {
            return new AddViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_addedit_trip_add, parent, false), destinationAddListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ((holder instanceof DestinationViewHolder)) {
            Destination destination = mDestinations.get(position - ITEMS_OFFSET);
            ((DestinationViewHolder) holder).bind(destination, destinationChangeListener, destinationDeleteListener, onSelectDurationListener);
        }
        else if (holder instanceof NameViewHolder) {
            ((NameViewHolder) holder).setTitle(mName);
        }
        else if (holder instanceof StartDateViewHolder) {
            ((StartDateViewHolder) holder).setStartDate(mStartDate);
        }
        else if (holder instanceof EndDateViewHolder) {
            ((EndDateViewHolder) holder).setEndDate(mEndDate);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == NAME_POSITION) {
            return NAME_ITEM_TYPE;
        }
        else if (position == START_DATE_POSITION) {
            return START_DATE_ITEM_TYPE;
        }
        else if (position == END_DATE_POSITION) {
            return END_DATE_ITEM_TYPE;
        }
        else if (position == getItemCount() - 1) {
            return ADD_ITEM_TYPE;
        }
        else return DESTINATION_ITEM_TYPE;
    }

    public void setTrip(Trip trip) {
        if (trip == null) return;

        mName = trip.getName();
        mStartDate = trip.getStartDate();
        mEndDate = trip.getEndDate();

        if (mDestinations == null) {
            mDestinations = new ArrayList<>();
        }
        else {
            mDestinations.clear();
        }
        if (trip.getDestinations() != null) {
            mDestinations.addAll(new ArrayList<Destination>(trip.getDestinations()));
        }

        notifyDataSetChanged();
    }

    public void setEndDate(Date date) {
        this.mEndDate = date;
        notifyItemChanged(END_DATE_POSITION);
    }

    @Override
    public int getItemCount() {
        return 1 + ITEMS_OFFSET + (mDestinations == null ? 0 : mDestinations.size());
    }

    @Override
    public void onNameChanged(String name) {
        mName = name;
    }

    @Override
    public void onStartDateChanged(Date startDate) {
        mStartDate = startDate;

        if (startDateChangeListener != null) {
            startDateChangeListener.onStartDateChanged(startDate);
        }
    }

    public String getName() {
        return mName;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public List<String> getDestinationOrderList() {
        List<String> destIdList = new ArrayList<>();
        if (mDestinations == null) return destIdList;

        for (Destination destination : mDestinations) {
            destIdList.add(destination.getDestinationId());
        }
        return destIdList;
    }

    public void setDestinationAddListener(OnAddDestinationListener destinationAddListener) {
        this.destinationAddListener = destinationAddListener;
    }

    public void setDestinationChangeListener(OnChangeDestinationListener destinationChangeListener) {
        this.destinationChangeListener = destinationChangeListener;
    }

    public void setDestinationDeleteListener(OnDeleteDestinationListener destinationDeleteListener) {
        this.destinationDeleteListener = destinationDeleteListener;
    }

    public void setStartDateChangeListener(StartDateViewHolder.OnStartDateChangeListener startDateChangeListener) {
        this.startDateChangeListener = startDateChangeListener;
    }

    public void setOnSelectDurationListener(OnSelectDurationListener onSelectDurationListener) {
        this.onSelectDurationListener = onSelectDurationListener;
    }

    public void changeDestination(String oldDestId, Destination destination) {
        if (mDestinations == null) return;
        for (int i = 0; i < mDestinations.size(); i++) {
            if (oldDestId.equals(mDestinations.get(i).getDestinationId())) {
                mDestinations.set(i, destination);
                notifyItemChanged(ITEMS_OFFSET + i);
                break;
            }
        }
    }

    public void deleteDestination(String oldDestId) {
        if (mDestinations == null) return;
        for (int i = 0; i < mDestinations.size(); i++) {
            if (oldDestId.equals(mDestinations.get(i).getDestinationId())) {
                mDestinations.remove(i);
                notifyItemRemoved(ITEMS_OFFSET + i);
                break;
            }
        }
    }

    public void addDestination(Destination destination) {
        if (mDestinations == null) {
            mDestinations = new ArrayList<>();
        }
        int position = mDestinations.size();
        mDestinations.add(destination);
        notifyItemInserted(ITEMS_OFFSET + position);
    }
}
