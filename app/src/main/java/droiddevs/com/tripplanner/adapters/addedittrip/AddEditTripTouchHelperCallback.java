package droiddevs.com.tripplanner.adapters.addedittrip;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import droiddevs.com.tripplanner.adapters.ItemTouchHelperAdapter;

/**
 * Created by elmira on 4/18/17.
 */

public class AddEditTripTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mItemTouchHelperAdapter;
    private static final String LOG_TAG = "AddEditTripTouchHelper";

    public AddEditTripTouchHelperCallback(ItemTouchHelperAdapter itemTouchHelperAdapter) {
        this.mItemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (!(viewHolder instanceof DestinationViewHolder))
            return ItemTouchHelper.ACTION_STATE_IDLE;
        int dragFlags = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
        int swapFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swapFlags);
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        mItemTouchHelperAdapter.onItemMove(fromPos, toPos);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.d(LOG_TAG, "onMove() viewHolder.getAdapterPosition(): " + viewHolder.getAdapterPosition() +
                ",  target.getAdapterPosition(): " + target.getAdapterPosition());

        return ((target instanceof DestinationViewHolder) && (viewHolder instanceof DestinationViewHolder));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mItemTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder instanceof DestinationViewHolder) {
            ((DestinationViewHolder) viewHolder).onItemSelected();
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof DestinationViewHolder) {
            ((DestinationViewHolder) viewHolder).onItemClear();
        }
    }
}
