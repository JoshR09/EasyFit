package com.example.easyfit.ui.workout.exercise;

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
import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.*;
import com.example.easyfit.databinding.FragmentExerciseBinding;
import com.example.easyfit.structures.CompletedWorkouts;
import com.example.easyfit.structures.Exercise;
import com.example.easyfit.structures.Set;
import com.example.easyfit.structures.Workout;
import com.example.easyfit.ui.workout.set.SetFragment;
import com.example.easyfit.ui.workout.WorkoutFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class ExerciseFragment extends Fragment implements ExerciseAdapter.OnItemClickListener {
    private FragmentExerciseBinding binding;

    private RecyclerView recyclerView;

    private List<Exercise> exercises;

    private Workout currentWorkout;

    private WorkoutFragment workoutFragment;

    private ExerciseAdapter adapter;

    private CompletedWorkouts completedWorkouts = new CompletedWorkouts();

    public void setCurrentWorkout(Workout workout) {
        this.currentWorkout = workout;
    }

    public ExerciseFragment(WorkoutFragment workoutFragment) {
        this.workoutFragment = workoutFragment;
    }

    public ExerciseAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable the callback to handle back button press
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, workoutFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExerciseBinding.inflate(inflater, container, false); // Initialize the binding
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.exerciseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadCompletedWorkouts();

        // Retrieve the workout from arguments bundle
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("workout")) {
            Workout workout = (Workout) bundle.getSerializable("workout");
            if (workout != null) {
                exercises = workout.getExercises();
                this.adapter = new ExerciseAdapter(requireContext(), exercises, this, this.recyclerView);
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

        Button completeWorkoutButton = root.findViewById(R.id.completeWorkout);
        completeWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completedWorkouts.addCompletedWorkout(new Workout(currentWorkout.getName(), currentWorkout.getExercises()));
                saveCompletedWorkouts();

                for (Exercise exercise : exercises) {
                    for (Set set : exercise.getSetList()) {
                        set.setComplete(false);
                    }
                    exercise.setLogged(false);
                }
                adapter.notifyDataSetChanged();
                workoutFragment.saveWorkouts();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, workoutFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }

    private void loadCompletedWorkouts() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("CompletedWorkoutPrefs", Context.MODE_PRIVATE);
        String completedWorkoutsJson = sharedPreferences.getString("completedWorkouts", "");

        if (!completedWorkoutsJson.isEmpty()) {
            Gson gson = new Gson();
            Type workoutListType = new TypeToken<HashMap<String, Workout>>(){}.getType();
            this.completedWorkouts.setCompletedWorkouts(gson.fromJson(completedWorkoutsJson, workoutListType));
        }
    }

    public void saveCompletedWorkouts() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("CompletedWorkoutPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String workoutsJson = gson.toJson(completedWorkouts.getCompletedWorkouts());
        editor.putString("completedWorkouts", workoutsJson);
        editor.apply();
    }

    private void showCreateExerciseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedDialogStyle);
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
                positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dialogButtonTextColor));

                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dialogButtonTextColor));

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

            if (workoutFragment != null) {
                workoutFragment.saveWorkouts(); // Call the saveWorkouts method in HomeFragment
            }
        }
    }

    public WorkoutFragment getHomeFragment() {
        return this.workoutFragment;
    }

    @Override
    public void onItemClick(Exercise exercise) {
        // Navigate to SetFragment and pass the selected exercise's setList
        SetFragment setFragment = new SetFragment(this, exercise);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sets", exercise.getSetList());
        bundle.putInt("position", exercises.indexOf(exercise));
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
        workoutFragment.saveWorkouts();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        this.workoutFragment.saveWorkouts();
    }

}