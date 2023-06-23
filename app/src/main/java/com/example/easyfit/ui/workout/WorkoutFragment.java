package com.example.easyfit.ui.workout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.R;
import com.example.easyfit.databinding.FragmentWorkoutsBinding;
import com.example.easyfit.structures.Exercise;
import com.example.easyfit.structures.Workout;
import com.example.easyfit.ui.workout.exercise.ExerciseFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkoutFragment extends Fragment implements WorkoutAdapter.OnItemClickListener {

    private FragmentWorkoutsBinding binding;

    private List<Workout> workouts = new ArrayList<>();

    private WorkoutAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable the callback to handle back button press
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Return to device's home screen
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkoutViewModel workoutViewModel =
                new ViewModelProvider(this).get(WorkoutViewModel.class);

        binding = FragmentWorkoutsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Display workouts in a RecyclerView or ListView
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        this.adapter = new WorkoutAdapter(requireContext(), workouts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button createWorkoutButton = binding.createWorkout;
        createWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateWorkoutDialog();
            }
        });

        loadWorkouts(); // Load the workouts from SharedPreferences

        return root;
    }

    @Override
    public void onItemClick(Workout workout) {
        // Navigate to ExerciseFragment and pass the selected workout
        ExerciseFragment exerciseFragment = new ExerciseFragment(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("workout", workout);
        exerciseFragment.setArguments(bundle);

        // Set the current workout in the ExerciseFragment
        exerciseFragment.setCurrentWorkout(workout);

        // Replace the current fragment with the ExerciseFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, exerciseFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showCreateWorkoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedDialogStyle);
        builder.setTitle("Enter Workout Name");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_workout, null);
        final EditText workoutNameEditText = dialogView.findViewById(R.id.workoutNameEditText);

        builder.setView(dialogView)
                .setPositiveButton("Create", null)
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
                        String workoutName = workoutNameEditText.getText().toString().trim();

                        if (workoutName.isEmpty()) {
                            workoutNameEditText.setError("Please enter workout name");
                            workoutNameEditText.requestFocus();
                        } else {
                            createWorkout(workoutName);
                            alertDialog.dismiss(); // Dismiss the dialog only if the input is valid
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }



    private void createWorkout(String name) {
        ArrayList<Exercise> exercises = new ArrayList<>();

        Workout workout = new Workout(name, exercises);
        workouts.add(workout);
        this.adapter.notifyDataSetChanged();

        saveWorkouts(); // Save the workouts using SharedPreferences

    }

    public void saveWorkouts() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String workoutsJson = gson.toJson(workouts);
        editor.putString("workouts", workoutsJson);
        editor.apply();
    }

    private void loadWorkouts() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE);
        String workoutsJson = sharedPreferences.getString("workouts", "");

        if (!workoutsJson.isEmpty()) {
            Gson gson = new Gson();
            Type workoutListType = new TypeToken<ArrayList<Workout>>(){}.getType();
            workouts = gson.fromJson(workoutsJson, workoutListType);

            if (workouts != null) {
                WorkoutAdapter adapter = new WorkoutAdapter(requireContext(), workouts, this);
                binding.recyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onDeleteClick(int position) {
        workouts.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, workouts.size()); // Update the remaining items
        saveWorkouts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}