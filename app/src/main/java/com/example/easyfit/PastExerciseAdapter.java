package com.example.easyfit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PastExerciseAdapter extends RecyclerView.Adapter<PastExerciseAdapter.PastExerciseViewHolder> {

    private List<Exercise> exercises;

    private PastExerciseAdapter.OnItemClickListener listener;

    public PastExerciseAdapter(List<Exercise> exercises, PastExerciseAdapter.OnItemClickListener listener) {
        this.exercises = exercises;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PastExerciseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_exercise, parent, false);
        return new PastExerciseAdapter.PastExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PastExerciseAdapter.PastExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.tvPastExerciseName.setText(exercise.getName());
        holder.tvPastExerciseSets.setText(String.valueOf(exercise.getSets()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(exercise);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class PastExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView tvPastExerciseName;
        TextView tvPastExerciseSets;

        public PastExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPastExerciseName = itemView.findViewById(R.id.tvPastExerciseName);
            tvPastExerciseSets = itemView.findViewById(R.id.tvPastExerciseSets);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Exercise exercise);
    }
}
