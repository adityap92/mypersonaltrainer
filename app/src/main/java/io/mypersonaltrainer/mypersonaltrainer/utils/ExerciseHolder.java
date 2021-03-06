package io.mypersonaltrainer.mypersonaltrainer.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to hold static variables for exercise information
 * created for easier and quicker access to data
 * Created by aditya on 7/17/17.
 */

public class ExerciseHolder {

    //instance variables
    public JSONObject obj;
    public JSONArray arr;
    public static ArrayList<String> exerciseNames;
    public ArrayList<Exercise> exerciseArrayList;
    public static Map<String, ArrayList<Exercise>> map;
    public static Map<String, Exercise> table;

    //constructor
    public ExerciseHolder() {
        exerciseNames = new ArrayList<>();
        table = new HashMap<>();
        map = new HashMap<>();
    }

    //create exercise map for expandablelistview
    public void setString(String json) {
        try {
            obj = new JSONObject(json);

            buildMap("Arms");
            buildMap("Back");
            buildMap("Chest");
            buildMap("Shoulder");
            buildMap("Legs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //build map and table from json string
    public void buildMap(String key) {
        try {
            exerciseArrayList = new ArrayList<>();
            arr = obj.getJSONArray(key);
            exerciseNames.add(key);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                Exercise e = new Exercise(o.getString("id"),
                        o.getString("name"),
                        o.getString("description"),
                        o.getString("vidUrl"),
                        key);
                exerciseArrayList.add(e);
                table.put(e.getId(), e);
            }
            map.put(key, exerciseArrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}


