package com.example.easyfit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.Exercise;
import com.example.easyfit.R;
import com.example.easyfit.Set;
import com.example.easyfit.SetAdapter;
import com.example.easyfit.databinding.FragmentSetBinding;
import com.example.easyfit.ui.ExerciseFragment;

import java.util.ArrayList;
import java.util.List;

public class SetFragment extends Fragment {
    private FragmentSetBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<Set> sets;
    private SetAdapter adapter;
    private ExerciseFragment exerciseFragment;
    private Exercise currentExercise;

    public SetFragment(ExerciseFragment exerciseFragment, Exercise currentExercise) {
        this.exerciseFragment = exerciseFragment;
        this.currentExercise = currentExercise;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable the callback to handle back button press
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, exerciseFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.setRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("sets")) {
            sets = (ArrayList<Set>) bundle.getSerializable("sets");
            if (sets != null) {
                adapter = new SetAdapter(sets);
                recyclerView.setAdapter(adapter);
            }
        }

        Button logButton = root.findViewById(R.id.logButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View view = recyclerView.getChildAt(i);
                    EditText etReps = view.findViewById(R.id.etReps);
                    EditText etWeight = view.findViewById(R.id.etWeight);

                    int reps = Integer.parseInt(etReps.getText().toString());
                    int weight = Integer.parseInt(etWeight.getText().toString());

                    if (adapter != null) {
                        adapter.updateSetValues(i, reps, weight);
                        exerciseFragment.getHomeFragment().saveWorkouts();
                    }
                }

                markExerciseAsLogged(); // New method to mark the exercise as logged

                getParentFragmentManager().popBackStack();
            }
        });

        Button addButton = root.findViewById(R.id.addSetButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the position of the clicked exercise
                int position = getArguments().getInt("position");

                // Add the new set to the exercise
                sets.add(new Set(0, 0));
                recyclerView.getAdapter().notifyDataSetChanged();

                // Update the number of sets in the ExerciseAdapter
                exerciseFragment.getAdapter().updateExerciseSets(position, sets.size());

                exerciseFragment.getHomeFragment().saveWorkouts();
            }
        });

        Button dlSetButton = root.findViewById(R.id.dlSetButton);
        dlSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sets.isEmpty()) {
                    int position = getArguments().getInt("position");
                    sets.remove(sets.size() - 1);
                    recyclerView.getAdapter().notifyItemRemoved(sets.size());
                    exerciseFragment.getAdapter().updateExerciseSets(position, sets.size());
                    exerciseFragment.getHomeFragment().saveWorkouts();
                }
            }
        });

        return root;
    }

    private void markExerciseAsLogged() {
        currentExercise.setLogged(true);
        exerciseFragment.getAdapter().notifyDataSetChanged();
        exerciseFragment.getHomeFragment().saveWorkouts();
    }
}


