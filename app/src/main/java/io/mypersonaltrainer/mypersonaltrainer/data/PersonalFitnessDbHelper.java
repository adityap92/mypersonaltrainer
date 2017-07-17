package io.mypersonaltrainer.mypersonaltrainer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aditya on 7/15/17.
 */

public class PersonalFitnessDbHelper extends SQLiteOpenHelper {

    private static  final String DATABASE_NAME = "personalFitness.db";

    private static final int DATABASE_VERSION = 1;

    public PersonalFitnessDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Define Users Table
        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " +
                DBContract.UsersEntry.TABLE_NAME + " (" +
                DBContract.UsersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.UsersEntry.COLUMN_USER_NAME + " TEXT NOT NULL, " +
                DBContract.UsersEntry.COLUMN_USER_GENDER + " TEXT NOT NULL, " +
                DBContract.UsersEntry.COLUMN_USER_HEIGHT + " TEXT NOT NULL, " +
                DBContract.UsersEntry.COLUMN_USER_WEIGHT + " TEXT NOT NULL, " +
                DBContract.UsersEntry.COLUMN_USER_ACTIVITY_LEVEL + " TEXT NOT NULL, " +
                DBContract.UsersEntry.COLUMN_USER_GOALS + " TEXT NOT NULL " +
                ");";
        //create Users Table
        db.execSQL(SQL_CREATE_USERS_TABLE);

        //define Planner Table
        final String SQL_CREATE_PLANNER_TABLE = "CREATE TABLE " +
                DBContract.PlannerEntry.TABLE_NAME + " (" +
                DBContract.PlannerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.PlannerEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                DBContract.PlannerEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                DBContract.PlannerEntry.COLUMN_WORKOUT_ID + " INTEGER NOT NULL" +
                ");";
        //create Planner Table
        db.execSQL(SQL_CREATE_PLANNER_TABLE);

        //define Planner Table
        final String SQL_CREATE_WORKOUT_TABLE = "CREATE TABLE " +
                DBContract.WorkoutEntry.TABLE_NAME + " (" +
                DBContract.WorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.WorkoutEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                DBContract.WorkoutEntry.COLUMN_EXERCISE_IDS + " INTEGER NOT NULL " +
                ");";
        //create Planner Table
        db.execSQL(SQL_CREATE_WORKOUT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DBContract.UsersEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.PlannerEntry.TABLE_NAME);
        onCreate(db);

    }
}
