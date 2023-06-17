package com.example.easyfit.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.*;
import com.example.easyfit.databinding.FragmentExerciseBinding;
import com.example.easyfit.ui.home.HomeFragment;
import com.google.gson.Gson;

import java.util.List;

public class ExerciseFragment extends Fragment implements ExerciseAdapter.OnItemClickListener {
    private FragmentExerciseBinding binding; // Add this line

    private RecyclerView recyclerView;
    private List<Exercise> exercises;

    private Workout currentWorkout;

    private HomeFragment homeFragment;

    public void setCurrentWorkout(Workout workout) {
        this.currentWorkout = workout;
    }

    public ExerciseFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExerciseBinding.inflate(inflater, container, false); // Initialize the binding
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.exerciseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Retrieve the workout from arguments bundle
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("workout")) {
            Workout workout = (Workout) bundle.getSerializable("workout");
            if (workout != null) {
                exercises = workout.getExercises();
                ExerciseAdapter adapter = new ExerciseAdapter(exercises, this);
                recyclerView.setAdapter(adapter);
            }
        }

        Button createExerciseButton = binding.addExercise;
        createExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateExerciseDialog();
            }
        });

        return root;
    }

    private void showCreateExerciseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Exercise");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_exercise, null);
        final EditText exerciseNameEditText = dialogView.findViewById(R.id.exerciseNameEditText);
        final EditText exerciseSetsEditText = dialogView.findViewById(R.id.exerciseSetsEditText);

        builder.setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String exerciseName = exerciseNameEditText.getText().toString();
                        int exerciseSets = Integer.parseInt(exerciseSetsEditText.getText().toString());
                        createExercise(exerciseName, exerciseSets);
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

    private void createExercise(String name, int sets) {
        Exercise newExercise = new Exercise(name, sets);

        if (currentWorkout != null) {
            currentWorkout.getExercises().add(newExercise);
            recyclerView.getAdapter().notifyDataSetChanged();

            if (homeFragment != null) {
                homeFragment.saveWorkouts(); // Call the saveWorkouts method in HomeFragment
            }
        }
    }

    @Override
    public void onItemClick(Exercise exercise) {
        // Navigate to SetFragment and pass the selected exercise's setList
        SetFragment setFragment = new SetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("sets", exercise.getSetList());
        setFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, setFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}







