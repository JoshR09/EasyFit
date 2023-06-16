package com.example.easyfit.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.R;
import com.example.easyfit.Workout;
import com.example.easyfit.WorkoutAdapter;
import com.example.easyfit.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private List<Workout> workouts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize workouts
        workouts = new ArrayList<>();
        workouts.add(new Workout("Push-ups", 10, 5));
        workouts.add(new Workout("Squats", 15, 7));
        workouts.add(new Workout("Plank", 5, 8));

        // Display workouts in a RecyclerView or ListView
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        WorkoutAdapter adapter = new WorkoutAdapter(workouts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button createWorkoutButton = binding.createWorkout;
        createWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateWorkoutDialog();
            }
        });

        return root;
    }

    private void showCreateWorkoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Create New Workout");

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_workout, null);
        builder.setView(dialogView);

        final EditText workoutNameEditText = dialogView.findViewById(R.id.editTextWorkoutName);
        final EditText workoutDurationEditText = dialogView.findViewById(R.id.editTextWorkoutDuration);
        final EditText workoutIntensityEditText = dialogView.findViewById(R.id.editTextWorkoutIntensity);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String workoutName = workoutNameEditText.getText().toString();
                int workoutDuration = Integer.parseInt(workoutDurationEditText.getText().toString());
                int workoutIntensity = Integer.parseInt(workoutIntensityEditText.getText().toString());

                Workout newWorkout = new Workout(workoutName, workoutDuration, workoutIntensity);
                workouts.add(newWorkout);
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}