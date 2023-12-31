package com.easydevelopment.easyfit.ui.history.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.easydevelopment.easyfit.R;
import com.easydevelopment.easyfit.databinding.FragmentPastExerciseBinding;
import com.easydevelopment.easyfit.structures.Exercise;
import com.easydevelopment.easyfit.structures.Workout;
import com.easydevelopment.easyfit.ui.history.set.PastSetFragment;
import com.easydevelopment.easyfit.ui.history.workout.PastWorkoutFragment;

import java.util.List;

public class PastExerciseFragment extends Fragment implements PastExerciseAdapter.OnItemClickListener{

    private FragmentPastExerciseBinding binding;

    private RecyclerView recyclerView;

    private List<Exercise> exercises;

    private PastExerciseAdapter adapter;

    private PastWorkoutFragment pastWorkoutFragment;

    public PastExerciseFragment(PastWorkoutFragment pastWorkoutFragment) {
        this.pastWorkoutFragment = pastWorkoutFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable the callback to handle back button press
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Replace the current fragment with the PastWorkoutFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, pastWorkoutFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

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
                exercises = workout.getLoggedExercises();
                this.adapter = new PastExerciseAdapter(exercises, this);
                recyclerView.setAdapter(this.adapter);
            }
        }

        return root;
    }

    @Override
    public void onItemClick(Exercise exercise) {
        PastSetFragment pastSetFragment = new PastSetFragment(exercise, this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sets", exercise.getCompletedSetList());
        bundle.putInt("position", exercises.indexOf(exercise));
        pastSetFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, pastSetFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
