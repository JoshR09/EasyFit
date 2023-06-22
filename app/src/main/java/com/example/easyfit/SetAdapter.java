package com.example.easyfit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SetViewHolder> {
    private ArrayList<Set> sets;

    private SetAdapter.OnItemClickListener listener;

    public SetAdapter(ArrayList<Set> sets, SetAdapter.OnItemClickListener listener) {
        this.sets = sets;
        this.listener = listener;
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
        holder.etReps.setText(String.valueOf(set.getReps()));
        holder.etWeight.setText(String.valueOf(set.getWeight()));

        if (set.isComplete()) {
            holder.itemView.setBackgroundResource(R.drawable.grey_overlay);
        } else {
            holder.itemView.setBackgroundResource(0);
        }

        holder.completeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (set.isComplete()) {
                    set.setComplete(false);
                } else {
                    set.setComplete(true);
                }
                listener.onCompleteClick(set);
                notifyDataSetChanged();
            }
        });

        holder.etReps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (set.isComplete()) {
                    set.setComplete(false);
                }
                listener.onCompleteClick(set);
                notifyDataSetChanged();
            }
        });

        holder.etWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (set.isComplete()) {
                    set.setComplete(false);
                }
                listener.onCompleteClick(set);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public void updateSetValues(int position, int reps, int weight) {
        Set set = sets.get(position);
        set.setReps(reps);
        set.setWeight(weight);
        notifyItemChanged(position);
    }

    public class SetViewHolder extends RecyclerView.ViewHolder {
        TextView tvReps;
        TextView tvWeight;
        EditText etReps;
        EditText etWeight;
        Button completeSet;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReps = itemView.findViewById(R.id.tvReps);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            etReps = itemView.findViewById(R.id.etReps);
            etWeight = itemView.findViewById(R.id.etWeight);
            completeSet = itemView.findViewById(R.id.completeSet);
        }
    }

    public interface OnItemClickListener {
        void onCompleteClick(Set set);
    }
}

