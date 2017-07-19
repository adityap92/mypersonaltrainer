package io.mypersonaltrainer.mypersonaltrainer.utils;

import java.io.Serializable;

/**
 * Created by aditya on 7/12/17.
 */

public class Exercise implements Serializable {

    String id;
    String exerciseName;
    String exerciseInstruction;
    String vidUrl;
    int weight, sets, reps;
    String muscGroup;

    public Exercise(String id,String exerciseName, String exerciseInstruction, String vidUrl, String muscGroup){
        this.id = id;
        this.exerciseName = exerciseName;
        this.exerciseInstruction = exerciseInstruction;
        this.vidUrl = vidUrl;
        this.weight = 0;
        this.sets = 3;
        this.reps = 10;
        this.muscGroup = muscGroup;
    }

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
}
