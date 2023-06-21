package com.example.easyfit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private List<Workout> workouts;

    private OnItemClickListener listener;

    private Context context;

    public WorkoutAdapter(Context context, List<Workout> workouts, OnItemClickListener listener) {
        this.context = context;
        this.workouts = workouts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.tvName.setText(workout.getName());

        // Set the click listener for the delete button
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the deleteItem method with the clicked position
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    deleteItem(clickedPosition);
                }
            }
        });

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

    public void deleteItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.RoundedDialogStyle);
        builder.setTitle("Delete Workout");
        builder.setMessage("Are you sure you want to delete this workout?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDeleteClick(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, workouts.size());
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

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageButton btnDelete;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Workout workout);
        void onDeleteClick(int position);
    }
}

