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
import com.example.easyfit.*;
import com.example.easyfit.databinding.FragmentPastSetBinding;
import com.example.easyfit.databinding.FragmentSetBinding;
import com.example.easyfit.ui.dashboard.DashboardFragment;

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
