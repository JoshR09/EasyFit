package com.example.easyfit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.Exercise;
import com.example.easyfit.ExerciseAdapter;
import com.example.easyfit.R;
import com.example.easyfit.Workout;

import java.util.List;

public class ExerciseFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Exercise> exercises;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);

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

        return root;
    }
}






