package io.mypersonaltrainer.mypersonaltrainer;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.mypersonaltrainer.mypersonaltrainer.data.DBContract;
import io.mypersonaltrainer.mypersonaltrainer.utils.ExerciseHolder;

/**
 * Planner Fragment to show workout exercises by day
 * Created by aditya on 7/10/17.
 */

public class PlannerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.tvMonday)
    TextView tvMonday;
    @BindView(R.id.lvDaysOfWeek)
    ListView lvWeekDays;
    @BindView(R.id.tvPlannerDate)
    TextView tvPlannerDate;
    private Unbinder unbinder;
    Context mContext;
    ArrayList<String> arrListWeek;
    static PlannerArrayAdapter adapter;
    boolean isTablet;
    int tabletPos;

    public PlannerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();

        //get formatted date to display
        final String formattedDate = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
        tvPlannerDate.setText(formattedDate);

        //get day of week and position in week array
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        List<String> arr = Arrays.asList(getResources().getStringArray(R.array.days_of_week));
        //position in array for use elsewhere
        final int posInWeek = arr.indexOf(dayOfTheWeek);

        //create array with this weeks dates
        setupDateArr(posInWeek);
        //check if tablet or not
        isTablet = getResources().getBoolean(R.bool.isTablet);

        //start loader for pulling Exercise data this week
        LoaderManager manager = getLoaderManager();
        Bundle bManager = new Bundle();
        bManager.putInt("pos", posInWeek);
        manager.initLoader(0, bManager, this);

        adapter = new PlannerArrayAdapter(mContext,
                R.layout.planner_view, retPlan(), posInWeek);
        lvWeekDays.setAdapter(adapter);
        ((MainActivity) mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //if phone
        if (!isTablet) {
            //open workout for today
            lvWeekDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    int diff = pos - posInWeek;
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, diff);

                    //pass date to workout fragment
                    WorkoutFragment workoutFragment = new WorkoutFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("date", new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
                    workoutFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, workoutFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else {
            //if tablet load master detail view
            tabletPos = posInWeek;
            WorkoutFragment workoutFragment = new WorkoutFragment();
            Bundle bundle = new Bundle();
            bundle.putString("date", new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
            workoutFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.workout_container, workoutFragment)
                    .commit();

            lvWeekDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    tabletPos = pos;
                    int diff = pos - posInWeek;
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, diff);

                    WorkoutFragment workoutFragment = new WorkoutFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("date", new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
                    workoutFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.workout_container, workoutFragment)
                            .addToBackStack(null)
                            .commit();
                    getLoaderManager().destroyLoader(0);
                    getLoaderManager().destroyLoader(1);
                    startLoader();
                    adapter.notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    //create empty array for adapter
    public ArrayList<Planner> retPlan() {
        ArrayList<Planner> plannerArr = new ArrayList<>();
        plannerArr.add(new Planner(getString(R.string.sunday), ""));
        plannerArr.add(new Planner(getString(R.string.monday), ""));
        plannerArr.add(new Planner(getString(R.string.tuesday), ""));
        plannerArr.add(new Planner(getString(R.string.wednesday), ""));
        plannerArr.add(new Planner(getString(R.string.thursday), ""));
        plannerArr.add(new Planner(getString(R.string.friday), ""));
        plannerArr.add(new Planner(getString(R.string.saturday), ""));
        return plannerArr;
    }

    //array of this weeks dates
    public void setupDateArr(int pos) {
        Calendar cal = Calendar.getInstance();
        arrListWeek = new ArrayList<>();
        int begin = 0 - pos;
        cal.add(Calendar.DATE, begin);
        for (int i = 0; i < 7; i++) {
            String startWeek = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            arrListWeek.add(startWeek);
            cal.add(Calendar.DATE, 1);
        }
    }

    //get muscle groups for each day
    public void getMuscleGroup(Cursor cur) {

        ArrayList<Planner> pa = adapter.getPlan();
        if (cur.moveToFirst()) {
            do {
                int index = arrListWeek.indexOf(cur.getString(cur.getColumnIndex(DBContract.WorkoutEntry.COLUMN_DATE)));
                String s = ExerciseHolder.table.get(cur.getString(cur.getColumnIndex(DBContract.WorkoutEntry.COLUMN_EXERCISE_ID))).getMuscGroup();
                String mg = pa.get(index).getMuscGroup();
                if (mg.equals("")) {
                    pa.set(index, new Planner(getResources().getStringArray(R.array.days_of_week)[index], s));
                } else if (!mg.contains(s))
                    pa.set(index, new Planner(getResources().getStringArray(R.array.days_of_week)[index], mg + ", " + s));
            } while (cur.moveToNext());
        }
        adapter.setPlan(pa);
    }

    //start loader to update view
    public void startLoader() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        List<String> arr = Arrays.asList(getResources().getStringArray(R.array.days_of_week));
        final int posInWeek = arr.indexOf(dayOfTheWeek);

        adapter.setPlan(retPlan());

        LoaderManager manager = getLoaderManager();
        Bundle bManager = new Bundle();
        bManager.putInt("pos", posInWeek);
        manager.initLoader(0, bManager, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().destroyLoader(0);
        getLoaderManager().destroyLoader(1);
        startLoader();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //handle both loader ids
        switch (id) {
            //queries planner table for workout IDs
            case 0:
                Calendar cal = Calendar.getInstance();

                int posInWeek = args.getInt("pos");

                int begin = 0 - posInWeek;
                cal.add(Calendar.DATE, begin);
                String startWeek = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

                cal = Calendar.getInstance();
                int end = 6 - posInWeek;
                cal.add(Calendar.DATE, end);
                String endWeek = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

                String[] projection = {
                        DBContract.PlannerEntry.COLUMN_DATE,
                        DBContract.PlannerEntry.COLUMN_WORKOUT_ID
                };

                String selection = DBContract.PlannerEntry.COLUMN_DATE + " BETWEEN \'" + startWeek + "\' AND \'" + endWeek + "\'";

                return new CursorLoader(mContext, DBContract.PlannerEntry.CONTENT_URI,
                        projection,
                        selection,
                        null, null);
            //queries Workout table with exercise IDs
            case 1:
                String[] selectionArgs = {};
                if (args != null) {
                    selectionArgs = args.getStringArray("ids");
                }
                String[] projection1 = {
                        DBContract.WorkoutEntry.COLUMN_DATE,
                        DBContract.WorkoutEntry.COLUMN_EXERCISE_ID
                };

                String selection1 = DBContract.WorkoutEntry.COLUMN_DATE + " IN(" + makePlaceholders(selectionArgs.length) + ")";


                return new CursorLoader(mContext, DBContract.WorkoutEntry.CONTENT_URI,
                        projection1, selection1, selectionArgs, null);
        }
        return null;

    }

    //helper method
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        //handle cursor return
        switch (loader.getId()) {
            case 0:
                String[] ids = new String[cursor.getCount()];
                if (cursor.moveToFirst()) {
                    do {
                        ids[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex(DBContract.PlannerEntry.COLUMN_DATE));
                    } while (cursor.moveToNext());
                }
                Bundle bundle = new Bundle();
                bundle.putStringArray("ids", ids);
                getLoaderManager().initLoader(1, bundle, this);
                break;
            case 1:
                getMuscleGroup(cursor);
                break;
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    //Custom Array Adapter for Planner Fragment
    public class PlannerArrayAdapter extends ArrayAdapter<Planner> {

        Context context;
        int layoutResourceId;
        ArrayList<Planner> plan;
        int p;

        public PlannerArrayAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Planner> data, int p) {
            super(context, resource);
            this.context = context;
            this.layoutResourceId = resource;
            this.plan = data;
            this.p = p;
        }

        public void setPlan(ArrayList<Planner> p) {
            this.plan = p;
            notifyDataSetChanged();
        }

        public ArrayList<Planner> getPlan() {
            return this.plan;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //populate views
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
            }

            TextView weekday = (TextView) row.findViewById(R.id.tvWeekDayPlanner);
            TextView muscGroup = (TextView) row.findViewById(R.id.tvGroupPlanner);

            if (plan.get(position) != null) {
                weekday.setText(plan.get(position).getWeekday());
                muscGroup.setText(plan.get(position).getMuscGroup());
            }

            if (!isTablet) {
                if (position == p) {
                    row.setBackgroundResource(R.color.colorAccent);
                } else
                    row.setBackgroundResource(android.R.color.white);

            } else {
                if (position == tabletPos) {
                    row.setBackgroundResource(R.color.colorAccent);
                } else
                    row.setBackgroundResource(android.R.color.white);
            }

            return row;
        }

        @Override
        public int getCount() {
            return plan.size();
        }
    }

    public class Planner {
        String weekday;
        String muscGroup;

        public Planner(String s, String s1) {
            this.weekday = s;
            this.muscGroup = s1;
        }

        public String getWeekday() {
            return weekday;
        }

        public String getMuscGroup() {
            return muscGroup;
        }
    }
}
