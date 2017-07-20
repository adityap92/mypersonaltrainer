package io.mypersonaltrainer.mypersonaltrainer;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.plus.PlusShare;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.mypersonaltrainer.mypersonaltrainer.data.DBContract;
import io.mypersonaltrainer.mypersonaltrainer.utils.Exercise;
import io.mypersonaltrainer.mypersonaltrainer.utils.ExerciseHolder;
import io.mypersonaltrainer.mypersonaltrainer.utils.Workout;

/**
 * Created by aditya on 7/10/17.
 */

public class WorkoutFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private Unbinder unbinder;
    @BindView(R.id.rvExercises)
    RecyclerView rvExercises;
    @BindView(R.id.fbAddExercise)
    FloatingActionButton fbAddExercise;
    @BindView(R.id.fbAddCustomExercise)
    FloatingActionButton fbShare;
    RecyclerView.LayoutManager rvLayoutManager;
    static RecyclerView.Adapter rvAdapter;
    Context mContext;
    @BindView(R.id.menu)
    FloatingActionMenu fab;
    public static Workout currWorkout;
    String formattedDate;

    public WorkoutFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view1 = inflater.inflate(R.layout.fragment_workout, container, false);
        unbinder = ButterKnife.bind(this, view1);
        mContext = getContext();

        Bundle bundle = getArguments();
        if(bundle!=null)
            formattedDate = bundle.getString("date");

        currWorkout = new Workout();


        getLoaderManager().initLoader(2,null,this);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if(!isTablet){
            ((MainActivity) mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            rvLayoutManager = new LinearLayoutManager(mContext);
            rvAdapter = new ExercisesAdapter(currWorkout);
            rvExercises.setLayoutManager(rvLayoutManager);
            rvExercises.setAdapter(rvAdapter);
        }else{
            rvLayoutManager = new GridLayoutManager(mContext,2);
            rvAdapter = new ExercisesAdapter(currWorkout);
            rvExercises.setLayoutManager(rvLayoutManager);
            rvExercises.setAdapter(rvAdapter);
        }



        fbAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.close(true);
                AddExerciseDialog dialog = new AddExerciseDialog();
                Bundle b = new Bundle();
                b.putString("date", formattedDate);
                dialog.setArguments(b);
                dialog.show(getFragmentManager(),"dialog");
            }
        });

        fbShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currWorkout.getExercises().size()>0){
                    String share="";
                    for(Exercise e : currWorkout.getExercises()){
                        share+=e.getExerciseName() + " Sets: "+e.getSets() + " Reps: " + e.getReps() +"\n";
                    }
                    Intent shareIntent = new PlusShare.Builder(mContext)
                            .setType("text/plain")
                            .setText(share)
                            .getIntent();
                    startActivityForResult(shareIntent,0);
                }else{
                    Snackbar snackbar = Snackbar.make(getView(), getString(R.string.add_workout),
                            Snackbar.LENGTH_LONG);
                    int snackbarTextId = android.support.design.R.id.snackbar_text;
                    View v = snackbar.getView();
                    TextView textView = (TextView) v.findViewById(snackbarTextId);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setTextColor(getResources().getColor(R.color.white, null));
                    }
                    snackbar.show();
                }

            }
        });

        fab.setClosedOnTouchOutside(true);

        return view1;
    }

    public void updateWorkouts(Cursor cur){
        currWorkout.getExercises().clear();
        if(cur.moveToFirst()){
            do{
                String index = cur.getString(cur.getColumnIndex(
                        DBContract.WorkoutEntry.COLUMN_EXERCISE_ID));
                String weight = cur.getString(cur.getColumnIndex(
                        DBContract.WorkoutEntry.COLUMN_WEIGHT));
                String sets = cur.getString(cur.getColumnIndex(
                        DBContract.WorkoutEntry.COLUMN_SETS));
                String reps = cur.getString(cur.getColumnIndex(
                        DBContract.WorkoutEntry.COLUMN_REPS));
                Exercise e = ExerciseHolder.table.get(index);
                e.setWeight(Integer.parseInt(weight));
                e.setSets(Integer.parseInt(sets));
                e.setReps(Integer.parseInt(reps));
                currWorkout.addExercise(e);
            }while(cur.moveToNext());
        }
        rvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch(id){
            case 2:
                String[] projection = {
                        DBContract.PlannerEntry.COLUMN_DATE,
                        DBContract.PlannerEntry.COLUMN_WORKOUT_ID
                };

                String selection = DBContract.PlannerEntry.COLUMN_DATE +
                        "= "+"\'"+formattedDate + "\'";

                return new CursorLoader(mContext, DBContract.PlannerEntry.CONTENT_URI,
                        projection,
                        selection,
                        null, null);
            case 3:
                String date="";
                if(args!=null){
                    date = args.getString("date");
                }
                String[] projection1 = {
                        DBContract.WorkoutEntry.COLUMN_DATE,
                        DBContract.WorkoutEntry.COLUMN_EXERCISE_ID,
                        DBContract.WorkoutEntry.COLUMN_WEIGHT,
                        DBContract.WorkoutEntry.COLUMN_SETS,
                        DBContract.WorkoutEntry.COLUMN_REPS
                };

                String selection1 = DBContract.WorkoutEntry.COLUMN_DATE + " = "+"\'"+date+"\' ";


                return new CursorLoader(mContext, DBContract.WorkoutEntry.CONTENT_URI,
                        projection1, selection1, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch(loader.getId()){
            case 2:
                String date = "";
                if (cursor.moveToFirst()) {
                    do {
                        date = cursor.getString(cursor.getColumnIndex(
                                DBContract.PlannerEntry.COLUMN_DATE));
                    } while (cursor.moveToNext());
                }
                Bundle bundle = new Bundle();
                bundle.putString("date",date);
                getLoaderManager().initLoader(3,bundle,this);
                break;
            case 3:
                updateWorkouts(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder>{

        Workout thisWorkout;

        public ExercisesAdapter(Workout workout){
            this.thisWorkout = workout;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.tvWorkoutCard)
            TextView tvName;
            @BindView(R.id.npWeight)
            NumberPicker npWeight;
            @BindView(R.id.npSets)
            NumberPicker npSets;
            @BindView(R.id.npReps)
            NumberPicker npReps;
            @BindView(R.id.bMoreInfo)
            Button bMoreInfo;
            @BindView(R.id.bDelete)
            Button bDelete;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                //setup number pickers
                npWeight.setMinValue(0);
                npWeight.setMaxValue(200);
                npSets.setMinValue(0);
                npSets.setMaxValue(5);
                npReps.setMinValue(0);
                npReps.setMaxValue(50);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_workout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final int pos = position;

            final Exercise e = thisWorkout.getExercises().get(position);

            holder.tvName.setText(e.getExerciseName());

            holder.bDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.are_u_sure)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String selection = DBContract
                                            .WorkoutEntry.COLUMN_EXERCISE_ID + " = ? AND "
                                            + DBContract.WorkoutEntry.COLUMN_DATE + " = "
                                            + "\'"+formattedDate + "\'";
                                    String[] selectionArgs = {e.getId()};

                                    int rows = mContext.getContentResolver().delete(
                                           DBContract.WorkoutEntry.CONTENT_URI,
                                           selection,
                                           selectionArgs);
                                    if(rows > 0){
                                        currWorkout.getExercises().remove(pos);
                                        rvAdapter.notifyDataSetChanged();
                                    }

                                }
                            })
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
            });

            holder.bMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExerciseDetailFragment detailFragment = new ExerciseDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("exercise", e);
                    detailFragment.setArguments(bundle);
                    //replace fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, detailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            holder.npWeight.setValue(e.getWeight());
            holder.npWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                    ContentValues cv = new ContentValues();
                    cv.put(DBContract.WorkoutEntry.COLUMN_WEIGHT, newVal);

                    String selection = DBContract.WorkoutEntry.COLUMN_EXERCISE_ID + " = ? ";
                    String[] selectionArgs = {e.getId()};
                    mContext.getContentResolver().update(DBContract.WorkoutEntry.CONTENT_URI,
                            cv,
                            selection,
                            selectionArgs
                            );
                }
            });

            holder.npSets.setValue(e.getSets());
            holder.npSets.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                    ContentValues cv = new ContentValues();
                    cv.put(DBContract.WorkoutEntry.COLUMN_SETS, newVal);

                    String selection = DBContract.WorkoutEntry.COLUMN_EXERCISE_ID + " = ? ";
                    String[] selectionArgs = {e.getId()};
                    mContext.getContentResolver().update(DBContract.WorkoutEntry.CONTENT_URI,
                            cv,
                            selection,
                            selectionArgs
                    );
                }
            });

            holder.npReps.setValue(e.getReps());
            holder.npReps.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                    ContentValues cv = new ContentValues();
                    cv.put(DBContract.WorkoutEntry.COLUMN_REPS, newVal);

                    String selection = DBContract.WorkoutEntry.COLUMN_EXERCISE_ID + " = ? ";
                    String[] selectionArgs = {e.getId()};
                    mContext.getContentResolver().update(DBContract.WorkoutEntry.CONTENT_URI,
                            cv,
                            selection,
                            selectionArgs
                    );
                }
            });
        }

        @Override
        public int getItemCount() {
            return thisWorkout.getExercises().size();
        }
    }
}
