package com.easydevelopment.easyfit.ui.history.set;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.easydevelopment.easyfit.R;
import com.easydevelopment.easyfit.databinding.FragmentPastSetBinding;
import com.easydevelopment.easyfit.structures.Exercise;
import com.easydevelopment.easyfit.structures.Set;
import com.easydevelopment.easyfit.ui.history.exercise.PastExerciseFragment;

import java.util.ArrayList;

public class PastSetFragment extends Fragment {

    private FragmentPastSetBinding binding;

    private RecyclerView recyclerView;

    private ArrayList<Set> sets;

    private PastSetAdapter adapter;

    private Exercise currentExercise;

    private PastExerciseFragment pastExerciseFragment;

    public PastSetFragment(Exercise currentExercise, PastExerciseFragment pastExerciseFragment) {
        this.currentExercise = currentExercise;
        this.pastExerciseFragment = pastExerciseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable the callback to handle back button press
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Replace the current fragment with the PastExerciseFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, pastExerciseFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPastSetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.pastSetRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        TextView exerciseName = root.findViewById(R.id.tvPastExerciseName);
        exerciseName.setText(currentExercise.getName().toUpperCase());

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("sets")) {
            sets = (ArrayList<Set>) bundle.getSerializable("sets");
            if (sets != null) {
                adapter = new PastSetAdapter(sets);
                recyclerView.setAdapter(adapter);
            }
        }

        return root;
    }

}
