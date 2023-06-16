package com.example.easyfit.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.R;
import com.example.easyfit.Exercise;
import com.example.easyfit.Workout;
import com.example.easyfit.WorkoutAdapter;
import com.example.easyfit.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private List<Workout> workouts = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize workouts
        ArrayList<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise("Push-ups", 10));
        exercises.add(new Exercise("Squats", 15));
        workouts.add(new Workout("Day 1", exercises));

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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Create Workout");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_workout, null);
        final EditText workoutNameEditText = dialogView.findViewById(R.id.workoutNameEditText);

        builder.setView(dialogView)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String workoutName = workoutNameEditText.getText().toString();
                        createWorkout(workoutName);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }

    private void createWorkout(String name) {
        ArrayList<Exercise> exercises = new ArrayList<>();
        // Add exercises to the workout as needed

        Workout workout = new Workout(name, exercises);
        workouts.add(workout);
        binding.recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}