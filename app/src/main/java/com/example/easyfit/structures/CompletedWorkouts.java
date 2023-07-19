package com.example.easyfit.structures;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CompletedWorkouts {

    HashMap<String, ArrayList<Workout>> completedWorkouts = new HashMap<>();

    public void addCompletedWorkout(Workout workout) {
        Calendar calendar = Calendar.getInstance();
        String dateKey = calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
        ArrayList<Workout> workoutsForDate = completedWorkouts.get(dateKey);
        if (workoutsForDate == null) {
            workoutsForDate = new ArrayList<>();
        }
        workoutsForDate.add(workout);
        completedWorkouts.put(dateKey, workoutsForDate);
    }

    public HashMap<String, ArrayList<Workout>> getCompletedWorkouts() {
        return completedWorkouts;
    }

    public void setCompletedWorkouts(HashMap<String, ArrayList<Workout>> completedWorkouts) {
        this.completedWorkouts = completedWorkouts;
    }
}
