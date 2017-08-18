package io.mypersonaltrainer.mypersonaltrainer;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mypersonaltrainer.mypersonaltrainer.data.DBContract;
import io.mypersonaltrainer.mypersonaltrainer.utils.ExerciseHolder;

/**
 * Created by aditya on 8/17/17.
 */

public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context mContext;
    @BindView(R.id.spinExercise)
    Spinner spinner;
    @BindView(R.id.lvHistory)
    ListView lvHistory;
    ArrayAdapter<String> spinnerArrayAdapter;
    ArrayAdapter<History> listViewAdapter;
    ArrayList<History> history;
    ArrayList<String> exercises;
    ArrayList<String> ids;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        final LoaderManager.LoaderCallbacks a = this;

        history = new ArrayList<>();
        exercises = new ArrayList<>();
        ids = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listViewAdapter = new HistoryAdapter(mContext,history);

        lvHistory.setAdapter(listViewAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                Bundle bundle = new Bundle();
                bundle.putString("id", ids.get(position));
                getSupportLoaderManager().restartLoader(1, bundle, a);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            //back arrow on actionbar pressed
            case android.R.id.home:
                onBackPressed();
                if (getResources().getBoolean(R.bool.isTablet)) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void populateSpinner(Cursor cursor) {

        if (cursor.moveToFirst()) {
            do {
                //read data from cursor
                String id = cursor.getString(cursor.getColumnIndex(DBContract.WorkoutEntry.COLUMN_EXERCISE_ID));
                ids.add(id);
                exercises.add(ExerciseHolder.table.get(id).getExerciseName());
            } while (cursor.moveToNext());
        }
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exercises); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void populateExerciseData(Cursor cursor) {
        history.clear();
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex(DBContract.WorkoutEntry.COLUMN_DATE));
                String num = cursor.getString(cursor.getColumnIndex(DBContract.WorkoutEntry.COLUMN_WEIGHT));
                history.add(new History(date, num));
            } while (cursor.moveToNext());
        }

        listViewAdapter.notifyDataSetChanged();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case 0:
                String[] projection = {
                        DBContract.WorkoutEntry.COLUMN_DATE,
                        DBContract.WorkoutEntry.COLUMN_EXERCISE_ID
                };

                String selection = " NOT " + DBContract.WorkoutEntry.COLUMN_DATE + " = ? " + " GROUP BY (" + DBContract.WorkoutEntry.COLUMN_EXERCISE_ID + ")";
                String[] selectionArgs = {""};
                return new CursorLoader(mContext, DBContract.WorkoutEntry.CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs, null);

            case 1:
                String id1 = args.getString("id");
                String[] projection1 = {
                        DBContract.WorkoutEntry.COLUMN_DATE,
                        DBContract.WorkoutEntry.COLUMN_EXERCISE_ID,
                        DBContract.WorkoutEntry.COLUMN_WEIGHT
                };

                String selection1 = DBContract.WorkoutEntry.COLUMN_EXERCISE_ID + " = ?";
                String[] selectionArgs1 = {id1};
                return new CursorLoader(mContext, DBContract.WorkoutEntry.CONTENT_URI,
                        projection1,
                        selection1,
                        selectionArgs1, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case 0:
                populateSpinner(data);
                break;
            case 1:
                populateExerciseData(data);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class HistoryAdapter extends ArrayAdapter<History>{

        public HistoryAdapter(Context context, ArrayList<History> histories){
            super(context,0,histories);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            History  hist = history.get(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.history_view, parent, false);
            }

            TextView tvDate = (TextView) convertView.findViewById(R.id.tvHistDate);
            TextView tvWeight = (TextView) convertView.findViewById(R.id.tvHistWeight);

            if(hist!=null){
                tvDate.setText(hist.getDate());
                tvWeight.setText(hist.getWeight());
            }


            return convertView;
        }
    }

    public class History{
        String date;
        String weight;

        public History(String date, String weight){
            this.date = date;
            this.weight = weight;
        }

        public String getDate() {
            return date;
        }

        public String getWeight() {
            return weight;
        }
    }
}
