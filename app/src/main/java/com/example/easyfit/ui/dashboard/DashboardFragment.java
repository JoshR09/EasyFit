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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.easyfit.*;
import com.example.easyfit.R;
import com.example.easyfit.databinding.FragmentDashboardBinding;
import com.example.easyfit.ui.ExerciseFragment;
import com.example.easyfit.ui.PastExerciseFragment;
import com.example.easyfit.ui.home.HomeFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private CompletedWorkouts completedWorkouts = new CompletedWorkouts();

    private MaterialCalendarView workoutHistory; // Attribute declaration

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        workoutHistory = root.findViewById(R.id.workoutHistory); // Initialize the attribute

        loadCompletedWorkouts();

        workoutHistory.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView calendarView, @NonNull CalendarDay date, boolean selected) {
                Calendar selectedDate = date.getCalendar();
                String dateKey = selectedDate.get(Calendar.DAY_OF_MONTH) + "-" + selectedDate.get(Calendar.MONTH) + "-" + selectedDate.get(Calendar.YEAR);
                Workout workout = completedWorkouts.getCompletedWorkouts().get(dateKey);
                if (workout != null) {
                    navigateToExerciseFragment(workout);
                }
            }
        });

        return root;
    }

    private Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void navigateToExerciseFragment(Workout workout) {
        PastExerciseFragment pastExerciseFragment = new PastExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("workout", workout);
        pastExerciseFragment.setArguments(bundle);

        // Replace the current fragment with the ExerciseFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, pastExerciseFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadCompletedWorkouts() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("CompletedWorkoutPrefs", Context.MODE_PRIVATE);
        String completedWorkoutsJson = sharedPreferences.getString("completedWorkouts", "");

        if (!completedWorkoutsJson.isEmpty()) {
            Gson gson = new Gson();
            Type workoutListType = new TypeToken<HashMap<String, Workout>>(){}.getType();
            this.completedWorkouts.setCompletedWorkouts(gson.fromJson(completedWorkoutsJson, workoutListType));

            // Set decorators for completed workout dates
            List<String> completedDates = new ArrayList<>();
            for (String completedDate : completedWorkouts.getCompletedWorkouts().keySet()) {
                completedDates.add(completedDate);
            }
            workoutHistory.addDecorator(new EventDecorator(requireContext(), completedDates));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Decorator class for highlighting completed workout dates
    private static class EventDecorator implements DayViewDecorator {
        private final List<String> dates;
        private final Context context;

        public EventDecorator(Context context, List<String> dates) {
            this.context = context;
            this.dates = dates;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            Calendar selectedDate = day.getCalendar();
            String dateKey = selectedDate.get(Calendar.DAY_OF_MONTH) + "-" + selectedDate.get(Calendar.MONTH) + "-" + selectedDate.get(Calendar.YEAR);
            return dates.contains(dateKey);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.blue_overlay));
        }
    }
}