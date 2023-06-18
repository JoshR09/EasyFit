package com.example.easyfit;

import java.util.Calendar;
import java.util.HashMap;

public class CompletedWorkouts {

    HashMap<String, Workout> completedWorkouts = new HashMap<>();

    public void addCompletedWorkout(Workout workout) {
        Calendar calendar = Calendar.getInstance();
        String dateKey = calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
        completedWorkouts.put(dateKey, workout);
    }

    public HashMap<String, Workout> getCompletedWorkouts() {
        return completedWorkouts;
    }

    public void setCompletedWorkouts(HashMap<String, Workout> completedWorkouts) {
        this.completedWorkouts = completedWorkouts;
    }
}
