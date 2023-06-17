package com.example.easyfit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SetViewHolder> {
    private ArrayList<Set> sets;

    public SetAdapter(ArrayList<Set> sets) {
        this.sets = sets;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false);
        return new SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        Set set = sets.get(position);
        holder.tvReps.setText(String.valueOf(set.getReps()));
        holder.tvWeight.setText(String.valueOf(set.getWeight()));
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public class SetViewHolder extends RecyclerView.ViewHolder {
        TextView tvReps;
        TextView tvWeight;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReps = itemView.findViewById(R.id.tvReps);
            tvWeight = itemView.findViewById(R.id.tvWeight);
        }
    }
}

