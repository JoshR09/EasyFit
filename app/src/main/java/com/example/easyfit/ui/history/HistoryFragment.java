package com.example.easyfit.ui.history;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.easyfit.R;
import com.example.easyfit.databinding.FragmentHistoryBinding;
import com.example.easyfit.structures.CompletedWorkouts;
import com.example.easyfit.structures.Workout;
import com.example.easyfit.ui.history.exercise.PastExerciseFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.*;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import java.lang.reflect.Type;
import java.util.*;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    private CompletedWorkouts completedWorkouts = new CompletedWorkouts();

    private MaterialCalendarView workoutHistory; // Attribute declaration

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable the callback to handle back button press
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Return to device's home screen
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HistoryViewModel historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        workoutHistory = root.findViewById(R.id.workoutHistory); // Initialize the attribute

        Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "lato_bold_2.ttf");

        DayViewDecorator customDecorator = new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                // Customize this method to determine which days to decorate
                return true;
            }

            @Override
            public void decorate(DayViewFacade view) {
                // Set the custom Typeface for the day text
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    view.addSpan(new TypefaceSpan(customFont));
                }
            }
        };

        // Customize the font for the month title
        workoutHistory.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                // Customize the font for the month title
                Typeface monthFont = Typeface.createFromAsset(getContext().getAssets(), "lato_bold_2.ttf");
                String monthLabel = getMonthName(day.getMonth()) + " " + day.getYear();
                SpannableString spannableString = new SpannableString(monthLabel);
                spannableString.setSpan(new CustomTypefaceSpan(monthFont), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spannableString;
            }

            private String getMonthName(int month) {
                String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                if (month >= 0 && month < monthNames.length) {
                    return monthNames[month];
                } else {
                    return "";
                }
            }
        });


        WeekDayFormatter customWeekDayFormatter = new WeekDayFormatter() {
            @Override
            public CharSequence format(int dayOfWeek) {
                // Customize the font for the weekdays
                Typeface weekdayFont = Typeface.createFromAsset(getContext().getAssets(), "lato_bold_2.ttf");
                String[] weekdayLabels = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
                SpannableString spannableString = new SpannableString(weekdayLabels[dayOfWeek - 1]);
                spannableString.setSpan(new CustomTypefaceSpan(weekdayFont), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spannableString;
            }
        };

        workoutHistory.setWeekDayFormatter(customWeekDayFormatter);

        workoutHistory.addDecorator(customDecorator);

        workoutHistory.setLeftArrowMask(getContext().getDrawable(R.drawable.left_calendar_arrow));

        workoutHistory.setRightArrowMask(getContext().getDrawable(R.drawable.right_calendar_arrow));

        loadCompletedWorkouts();

        workoutHistory.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView calendarView, @NonNull CalendarDay date, boolean selected) {
                Calendar selectedDate = date.getCalendar();
                String dateKey = selectedDate.get(Calendar.DAY_OF_MONTH) + "-" + selectedDate.get(Calendar.MONTH) + "-" + selectedDate.get(Calendar.YEAR);
                Workout workout = completedWorkouts.getCompletedWorkouts().get(dateKey);
                if (workout != null) {
                    navigateToPastExerciseFragment(workout);
                }
            }
        });

        return root;
    }

    public void navigateToPastExerciseFragment(Workout workout) {
        PastExerciseFragment pastExerciseFragment = new PastExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("workout", workout);
        pastExerciseFragment.setArguments(bundle);

        // Replace the current fragment with the PastExerciseFragment
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
            view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.date_background));
            view.addSpan(new ForegroundColorSpan(Color.WHITE));
        }
    }

    private static class CustomTypefaceSpan extends TypefaceSpan {
        private final Typeface newType;

        public CustomTypefaceSpan(Typeface type) {
            super("");
            newType = type;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeFace(paint, newType);
        }

        private static void applyCustomTypeFace(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTypeface(tf);
        }
    }

}