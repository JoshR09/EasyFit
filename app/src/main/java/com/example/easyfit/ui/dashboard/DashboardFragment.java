package com.example.easyfit.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.easyfit.*;
import com.example.easyfit.databinding.FragmentDashboardBinding;
import com.example.easyfit.ui.ExerciseFragment;
import com.example.easyfit.ui.home.HomeFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private CompletedWorkouts completedWorkouts = new CompletedWorkouts();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loadCompletedWorkouts();

        CalendarView workoutHistory = root.findViewById(R.id.workoutHistory);
        workoutHistory.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String calendarViewDate = day + "-" + month + "-" + year;
                Workout workout = completedWorkouts.getCompletedWorkouts().get(calendarViewDate);
                if (workout != null) {
                    navigateToExerciseFragment();
                }
            }
        });

        return root;
    }

    public void navigateToExerciseFragment() {
    }

    private void loadCompletedWorkouts() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("CompletedWorkoutPrefs", Context.MODE_PRIVATE);
        String completedWorkoutsJson = sharedPreferences.getString("completedWorkouts", "");

        if (!completedWorkoutsJson.isEmpty()) {
            Gson gson = new Gson();
            Type workoutListType = new TypeToken<HashMap<String, Workout>>(){}.getType();
            this.completedWorkouts.setCompletedWorkouts(gson.fromJson(completedWorkoutsJson, workoutListType));

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}