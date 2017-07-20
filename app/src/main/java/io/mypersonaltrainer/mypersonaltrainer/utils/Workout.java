package io.mypersonaltrainer.mypersonaltrainer.utils;

import java.util.ArrayList;

/**
 * Created by aditya on 7/12/17.
 */

public class Workout {

    //instance variables
    ArrayList<Exercise> exercises;

    //constructor
    public Workout(){
        this.exercises = new ArrayList<Exercise>();
    }

    //add exercises to workout object
    public void addExercise(Exercise newExercise){
        this.exercises.add(newExercise);
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }
}
