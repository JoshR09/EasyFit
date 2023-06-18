package com.example.easyfit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.databinding.FragmentSetBinding;
import com.example.easyfit.ui.ExerciseFragment;

import java.util.ArrayList;

public class SetFragment extends Fragment {
    private FragmentSetBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<Set> sets;
    private SetAdapter adapter;

    private ExerciseFragment exerciseFragment;

    public SetFragment(ExerciseFragment exerciseFragment) {
        this.exerciseFragment = exerciseFragment;
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

                getParentFragmentManager().popBackStack();
            }
        });

        return root;
    }
}


