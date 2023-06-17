package com.example.easyfit.ui.home;

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
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.R;
import com.example.easyfit.Exercise;
import com.example.easyfit.Workout;
import com.example.easyfit.WorkoutAdapter;
import com.example.easyfit.databinding.FragmentHomeBinding;
import com.example.easyfit.ui.ExerciseFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements WorkoutAdapter.OnItemClickListener {

    private FragmentHomeBinding binding;

    private List<Workout> workouts = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Display workouts in a RecyclerView or ListView
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        WorkoutAdapter adapter = new WorkoutAdapter(workouts, this);
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

    @Override
    public void onItemClick(Workout workout) {
        // Navigate to ExerciseFragment and pass the selected workout
        ExerciseFragment exerciseFragment = new ExerciseFragment();
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