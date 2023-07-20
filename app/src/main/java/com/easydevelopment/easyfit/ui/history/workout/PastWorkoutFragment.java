package com.easydevelopment.easyfit.ui.history.workout;

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
import com.easydevelopment.easyfit.databinding.FragmentPastWorkoutBinding;
import com.easydevelopment.easyfit.structures.Workout;
import com.easydevelopment.easyfit.ui.history.HistoryFragment;
import com.easydevelopment.easyfit.ui.history.exercise.PastExerciseFragment;

import java.util.ArrayList;

public class PastWorkoutFragment extends Fragment implements PastWorkoutAdapter.OnItemClickListener {

    private FragmentPastWorkoutBinding binding;

    private RecyclerView recyclerView;

    private ArrayList<Workout> workouts;

    private PastWorkoutAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable the callback to handle back button press
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate to the dashboard fragment
                HistoryFragment historyFragment = new HistoryFragment();

                // Replace the current fragment with the PastExerciseFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, historyFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPastWorkoutBinding.inflate(inflater, container, false); // Initialize the binding
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.pastWorkoutRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Retrieve the workouts from arguments bundle
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("workouts")) {
            ArrayList<Workout> workouts = (ArrayList<Workout>) bundle.getSerializable("workouts");
            if (workouts != null) {
                this.workouts = workouts;
                this.adapter = new PastWorkoutAdapter(this.workouts, this);
                recyclerView.setAdapter(this.adapter);
            }
        }

        return root;
    }

    @Override
    public void onItemClick(Workout workout) {
        // Navigate to ExerciseFragment and pass the selected workout
        PastExerciseFragment pastExerciseFragment = new PastExerciseFragment(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("workout", workout);
        pastExerciseFragment.setArguments(bundle);

        // Replace the current fragment with the ExerciseFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, pastExerciseFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
