package com.example.easyfit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PastSetAdapter extends RecyclerView.Adapter<PastSetAdapter.PastSetViewHolder> {

    private ArrayList<Set> sets;

    public PastSetAdapter(ArrayList<Set> sets) {
        this.sets = sets;
    }

    @NonNull
    @Override
    public PastSetAdapter.PastSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_set, parent, false);
        return new PastSetAdapter.PastSetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastSetAdapter.PastSetViewHolder holder, int position) {
        Set set = sets.get(position);
        holder.tvPastRepsValue.setText(String.valueOf(set.getReps()));
        holder.tvPastWeightValue.setText(String.valueOf(set.getWeight()));
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public class PastSetViewHolder extends RecyclerView.ViewHolder {
        TextView tvPastReps;
        TextView tvPastWeight;
        TextView tvPastRepsValue;
        TextView tvPastWeightValue;

        public PastSetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPastReps = itemView.findViewById(R.id.tvPastReps);
            tvPastWeight = itemView.findViewById(R.id.tvPastWeight);
            tvPastRepsValue = itemView.findViewById(R.id.tvPastRepsValue);
            tvPastWeightValue = itemView.findViewById(R.id.tvPastWeightValue);
        }
    }
}
