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

    public Exercise(String id,String exerciseName, String exerciseInstruction, String vidUrl){
        this.id = id;
        this.exerciseName = exerciseName;
        this.exerciseInstruction = exerciseInstruction;
        this.vidUrl = vidUrl;
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
}
