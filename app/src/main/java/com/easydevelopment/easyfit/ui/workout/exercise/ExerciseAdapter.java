package com.easydevelopment.easyfit.ui.workout.exercise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.easydevelopment.easyfit.R;
import com.easydevelopment.easyfit.structures.Exercise;

import java.util.Collections;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> implements ExerciseItemTouchHelper.ItemTouchHelperListener {
    private List<Exercise> exercises;

    private ExerciseAdapter.OnItemClickListener listener;

    private Context context;

    public ExerciseAdapter(Context context, List<Exercise> exercises, ExerciseAdapter.OnItemClickListener listener, RecyclerView recyclerView) {
        this.context = context;
        this.exercises = exercises;
        this.listener = listener;

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ExerciseItemTouchHelper(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // Implement the necessary methods for drag-and-drop functionality
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        // Swap the exercises in the list
        Collections.swap(exercises, fromPosition, toPosition);

        // Notify the adapter that the item has moved
        notifyItemMoved(fromPosition, toPosition);

        listener.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        // Not used for drag-and-drop, but required to implement the interface
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.tvExerciseText.setText(exercise.getName() + ", " + String.valueOf(exercise.getSets()) + " sets");

        if (exercise.isLogged()) {
            holder.linearLayout.setBackgroundResource(R.drawable.green_green_gradient);
        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.white_button);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(exercise);
            }
        });

        // Set the click listener for the delete button
        holder.dlExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the deleteItem method with the clicked position
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    deleteItem(clickedPosition);
                }
            }
        });
    }

    public void deleteItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.RoundedDialogStyle);
        builder.setTitle("Delete Exercise");
        builder.setMessage("Are you sure you want to delete this exercise?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDeleteClick(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, exercises.size());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextColor(ContextCompat.getColor(context, R.color.dialogButtonTextColor));

                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(ContextCompat.getColor(context, R.color.dialogButtonTextColor));
            }
        });


        dialog.show();
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void updateExerciseSets(int position, int sets) {
        Exercise exercise = exercises.get(position);
        exercise.setSets(sets);
        notifyItemChanged(position);
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseText;
        ImageButton dlExerciseButton;
        LinearLayout linearLayout;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseText = itemView.findViewById(R.id.tvExerciseText);
            dlExerciseButton = itemView.findViewById(R.id.dlExerciseButton);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Exercise exercise);
        void onDeleteClick(int position);
        void onItemMove(int fromPosition, int toPosition);
    }
}
