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

    private ExerciseAdapter adapter;

    public void setCurrentWorkout(Workout workout) {
        this.currentWorkout = workout;
    }

    public ExerciseFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    public Workout getCurrentWorkout() {
        return currentWorkout;
    }

    public ExerciseAdapter getAdapter() {
        return adapter;
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
                this.adapter = new ExerciseAdapter(requireContext(), exercises, this);
                recyclerView.setAdapter(this.adapter);
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
                .setPositiveButton("Add", null) // Set positive button to null initially
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alertDialog = builder.create(); // Create the AlertDialog

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String exerciseName = exerciseNameEditText.getText().toString().trim();
                        String exerciseSetsText = exerciseSetsEditText.getText().toString().trim();

                        if (exerciseName.isEmpty()) {
                            exerciseNameEditText.setError("Please enter exercise name");
                            exerciseNameEditText.requestFocus();
                        } else if (exerciseSetsText.isEmpty()) {
                            exerciseSetsEditText.setError("Please enter number of sets");
                            exerciseSetsEditText.requestFocus();
                        } else {
                            int exerciseSets = Integer.parseInt(exerciseSetsText);
                            createExercise(exerciseName, exerciseSets);
                            alertDialog.dismiss(); // Dismiss the dialog only if the input is valid
                        }
                    }
                });
            }
        });

        alertDialog.show();
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

    public HomeFragment getHomeFragment() {
        return this.homeFragment;
    }

    @Override
    public void onItemClick(Exercise exercise) {
        // Navigate to SetFragment and pass the selected exercise's setList
        SetFragment setFragment = new SetFragment(this, exercise);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sets", exercise.getSetList());
        setFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, setFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onDeleteClick(int position) {
        exercises.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, exercises.size()); // Update the remaining items
        homeFragment.saveWorkouts();
    }

}







