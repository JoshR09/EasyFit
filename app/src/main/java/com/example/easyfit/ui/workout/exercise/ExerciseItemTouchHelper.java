package com.example.easyfit.ui.workout.exercise;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseItemTouchHelper extends ItemTouchHelper.Callback {
    private final ItemTouchHelperListener listener;

    public ExerciseItemTouchHelper(ItemTouchHelperListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Specify the drag directions (up and down)
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // Specify the swipe directions (none)
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // Notify the listener that an item has been moved
        listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Not used for drag-and-drop, but required to implement the Callback
    }

    public interface ItemTouchHelperListener {
        void onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }
}

