package com.example.easyfit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.Exercise;
import com.example.easyfit.ExerciseAdapter;
import com.example.easyfit.R;
import com.example.easyfit.Workout;
import com.example.easyfit.databinding.FragmentExerciseBinding;

import java.util.List;

public class ExerciseFragment extends Fragment {
    private FragmentExerciseBinding binding; // Add this line

    private RecyclerView recyclerView;
    private List<Exercise> exercises;

    private Workout currentWorkout;

    public void setCurrentWorkout(Workout workout) {
        this.currentWorkout = workout;
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
                ExerciseAdapter adapter = new ExerciseAdapter(exercises);
                recyclerView.setAdapter(adapter);
            }
        }

        Button createExerciseButton = binding.addExercise;
        createExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new exercise
                Exercise newExercise = new Exercise("New Exercise", 0);

                // Add the new exercise to the current workout
                if (currentWorkout != null) {
                    currentWorkout.getExercises().add(newExercise);
                    // Notify the adapter that the data has changed
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        return root;
    }
}







