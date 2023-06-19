package com.example.easyfit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.*;
import com.example.easyfit.databinding.FragmentPastSetBinding;
import com.example.easyfit.databinding.FragmentSetBinding;

import java.util.ArrayList;

public class PastSetFragment extends Fragment {

    private FragmentPastSetBinding binding;

    private RecyclerView recyclerView;

    private ArrayList<Set> sets;

    private PastSetAdapter adapter;

    private Exercise currentExercise;

    public PastSetFragment(Exercise currentExercise) {
        this.currentExercise = currentExercise;
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
