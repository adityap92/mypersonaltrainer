package io.mypersonaltrainer.mypersonaltrainer.utils;

import java.io.Serializable;

/**
 * Exercise object to store data for easier access
 * Created by aditya on 7/12/17.
 */

public class Exercise implements Serializable {

    //instance variables
    String id;
    String exerciseName;
    String exerciseInstruction;
    String vidUrl;
    int weight, sets, reps;
    String muscGroup;

    //constructor
    public Exercise(String id, String exerciseName, String exerciseInstruction, String vidUrl, String muscGroup) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.exerciseInstruction = exerciseInstruction;
        this.vidUrl = vidUrl;
        this.weight = 0;
        this.sets = 3;
        this.reps = 10;
        this.muscGroup = muscGroup;
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public String getExerciseInstruction() {
        return exerciseInstruction;
    }

    public String getVidUrl() {
        return vidUrl;
    }

    public String getMuscGroup() {
        return muscGroup;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }
}
