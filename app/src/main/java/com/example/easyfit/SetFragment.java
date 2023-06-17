package com.example.easyfit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easyfit.databinding.FragmentSetBinding;

import java.util.ArrayList;

public class SetFragment extends Fragment {
    private FragmentSetBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<Set> sets;

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
                SetAdapter adapter = new SetAdapter(sets);
                recyclerView.setAdapter(adapter);
            }
        }

        return root;
    }
}

