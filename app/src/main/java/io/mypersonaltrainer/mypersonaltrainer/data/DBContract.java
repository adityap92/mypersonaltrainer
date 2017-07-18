package io.mypersonaltrainer.mypersonaltrainer.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by aditya on 7/15/17.
 */

public class DBContract {

    //Constants for ContentProvider
    public static final String AUTHORITY = "io.mypersonaltrainer.mypersonaltrainer";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_USERS = "users";
    public static final String PATH_PLANNER = "planner";
    public static final String PATH_WORKOUT = "workout";


    public static final class UsersEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();

        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USER_NAME = "name";
        public static final String COLUMN_USER_GENDER = "gender";
        public static final String COLUMN_USER_HEIGHT = "height";
        public static final String COLUMN_USER_WEIGHT = "weight";
        public static final String COLUMN_USER_ACTIVITY_LEVEL = "activityLevel";
    }

    public static final class PlannerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANNER).build();

        public static final String TABLE_NAME = "planner";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WORKOUT_ID = "workoutId";

    }

    public static final class WorkoutEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORKOUT).build();

        public static final String TABLE_NAME = "workout";
        public static final String COLUMN_EXERCISE_ID = "exerciseId";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_SETS = "reps";
        public static final String COLUMN_REPS = "sets";
    }

}
