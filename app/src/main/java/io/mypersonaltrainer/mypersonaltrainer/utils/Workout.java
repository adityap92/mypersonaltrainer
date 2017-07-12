package io.mypersonaltrainer.mypersonaltrainer.utils;

import java.util.ArrayList;

/**
 * Created by aditya on 7/12/17.
 */

public class Workout {

    ArrayList<Exercise> exercises;
    String muscleGroup;

    public Workout(String muscleGroup){
        this.exercises = new ArrayList<Exercise>();
        this.muscleGroup = muscleGroup;
    }

    public void addExercise(Exercise newExercise){
        this.exercises.add(newExercise);
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }
}
