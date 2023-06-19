package com.example.easyfit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.*;
import com.example.easyfit.databinding.FragmentExerciseBinding;
import com.example.easyfit.databinding.FragmentPastExerciseBinding;

import java.util.List;

public class PastExerciseFragment extends Fragment implements PastExerciseAdapter.OnItemClickListener{

    private FragmentPastExerciseBinding binding;

    private RecyclerView recyclerView;

    private List<Exercise> exercises;

    private PastExerciseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPastExerciseBinding.inflate(inflater, container, false); // Initialize the binding
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.pastExerciseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Retrieve the workout from arguments bundle
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("workout")) {
            Workout workout = (Workout) bundle.getSerializable("workout");
            if (workout != null) {
                exercises = workout.getExercises();
                this.adapter = new PastExerciseAdapter(exercises, this);
                recyclerView.setAdapter(this.adapter);
            }
        }

        return root;
    }

    @Override
    public void onItemClick(Exercise exercise) {
        PastSetFragment pastSetFragment = new PastSetFragment(exercise);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sets", exercise.getSetList());
        bundle.putInt("position", exercises.indexOf(exercise));
        pastSetFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, pastSetFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
