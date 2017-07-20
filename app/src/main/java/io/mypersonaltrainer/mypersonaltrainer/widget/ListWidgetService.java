package io.mypersonaltrainer.mypersonaltrainer.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.mypersonaltrainer.mypersonaltrainer.R;
import io.mypersonaltrainer.mypersonaltrainer.data.DBContract;
import io.mypersonaltrainer.mypersonaltrainer.utils.ExerciseHolder;

/**
 * Created by aditya on 7/19/17.
 */

public class ListWidgetService extends RemoteViewsService {

    Context mContext;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        mContext = getApplicationContext();
        return (new ListRemoteViewsFactory(mContext));
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    ArrayList<WidgetEx> exercises;

    public ListRemoteViewsFactory(Context c) {
        this.mContext = c;
    }

    @Override
    public void onCreate() {
        exercises = new ArrayList<WidgetEx>();
    }

    //query database for todays exercises
    @Override
    public void onDataSetChanged() {
        Cursor cursor;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        String[] projection = {DBContract.PlannerEntry.COLUMN_DATE,
                DBContract.PlannerEntry.COLUMN_WORKOUT_ID};
        String selection = DBContract.PlannerEntry.COLUMN_DATE + " = " + "\'" + date + "\'";
        cursor = mContext.getContentResolver().query(DBContract.PlannerEntry.CONTENT_URI,
                projection,
                selection,
                null, null
        );
        String[] workoutIDs = new String[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                workoutIDs[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex(DBContract.PlannerEntry.COLUMN_DATE));
            } while (cursor.moveToNext());
        }
        cursor.close();

        projection = new String[]{DBContract.WorkoutEntry.COLUMN_EXERCISE_ID,
                DBContract.WorkoutEntry.COLUMN_EXERCISE_ID,
                DBContract.WorkoutEntry.COLUMN_SETS,
                DBContract.WorkoutEntry.COLUMN_REPS};
        selection = DBContract.WorkoutEntry.COLUMN_DATE + " IN("
                + makePlaceholders(workoutIDs.length) + ")";

        cursor = mContext.getContentResolver().query(DBContract.WorkoutEntry.CONTENT_URI,
                projection,
                selection,
                workoutIDs,
                null, null);

        if (cursor.moveToFirst()) {
            do {
                String n = ExerciseHolder.table.get(
                        cursor.getString(cursor.getColumnIndex(
                                DBContract.WorkoutEntry.COLUMN_EXERCISE_ID))).getExerciseName();
                int s = ExerciseHolder.table.get(
                        cursor.getString(cursor.getColumnIndex(
                                DBContract.WorkoutEntry.COLUMN_EXERCISE_ID))).getSets();
                int r = ExerciseHolder.table.get(
                        cursor.getString(cursor.getColumnIndex(
                                DBContract.WorkoutEntry.COLUMN_EXERCISE_ID))).getReps();
                exercises.add(new WidgetEx(n, String.valueOf(s), String.valueOf(r)));
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            //throw new RuntimeException("No placeholders");
            return null;
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    //update widget view
    @Override
    public RemoteViews getViewAt(int pos) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_list_item);
        views.setTextViewText(R.id.tvWidgetName, exercises.get(pos).getName());
        views.setTextViewText(R.id.tvWidgetSets, exercises.get(pos).getSets());
        views.setTextViewText(R.id.tvWidgetReps, exercises.get(pos).getReps());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //widget POJO for easier data access
    public class WidgetEx {
        String name, sets, reps;

        public WidgetEx(String n, String s, String r) {
            this.name = n;
            this.sets = s;
            this.reps = r;
        }

        public String getName() {
            return name;
        }

        public String getSets() {
            return sets;
        }

        public String getReps() {
            return reps;
        }
    }
}
