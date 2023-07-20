package com.easydevelopment.easyfit.ui.history.workout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.easydevelopment.easyfit.R;
import com.easydevelopment.easyfit.structures.Workout;

import java.util.List;

public class PastWorkoutAdapter extends RecyclerView.Adapter<PastWorkoutAdapter.PastWorkoutViewHolder> {
    private List<Workout> workouts;

    private PastWorkoutAdapter.OnItemClickListener listener;


    public PastWorkoutAdapter(List<Workout> workouts, PastWorkoutAdapter.OnItemClickListener listener) {
        this.workouts = workouts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PastWorkoutAdapter.PastWorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_workout, parent, false);
        return new PastWorkoutAdapter.PastWorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastWorkoutAdapter.PastWorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.tvPastWorkoutName.setText(workout.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(workout);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public class PastWorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView tvPastWorkoutName;

        public PastWorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPastWorkoutName = itemView.findViewById(R.id.tvPastWorkoutName);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Workout workout);
    }
}
