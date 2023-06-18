package com.example.easyfit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises;

    private ExerciseAdapter.OnItemClickListener listener;

    private Context context;

    public ExerciseAdapter(Context context, List<Exercise> exercises, ExerciseAdapter.OnItemClickListener listener) {
        this.context = context;
        this.exercises = exercises;
        this.listener = listener;
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
        holder.tvExerciseName.setText(exercise.getName());
        holder.tvExerciseSets.setText(String.valueOf(exercise.getSets()));

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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName;
        TextView tvExerciseSets;
        Button dlExerciseButton;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvExerciseSets = itemView.findViewById(R.id.tvExerciseSets);
            dlExerciseButton = itemView.findViewById(R.id.dlExerciseButton);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Exercise exercise);
        void onDeleteClick(int position);
    }
}
