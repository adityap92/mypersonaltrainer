package io.mypersonaltrainer.mypersonaltrainer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.mypersonaltrainer.mypersonaltrainer.utils.Exercise;
import io.mypersonaltrainer.mypersonaltrainer.utils.Workout;

/**
 * Created by aditya on 7/10/17.
 */

public class WorkoutFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.rvExercises)
    RecyclerView rvExercises;
    RecyclerView.LayoutManager rvLayoutManager;
    RecyclerView.Adapter rvAdapter;
    Context mContext;
//    @BindView(R.id.fab)
//    FloatingActionButton fab;

    public WorkoutFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();

        Workout armWorkout = new Workout("Arms");
        armWorkout.addExercise(new Exercise("Bicep Curl", "Curl Arm","ykJmrZ5v0Oo"));
        armWorkout.addExercise(new Exercise("Tricep Extension", "Extend Tricep","_gsUck-7M74"));
        armWorkout.addExercise(new Exercise("Shoulder Press", "Lift weight with shoulders","B-aVuyhvLHU"));
        armWorkout.addExercise(new Exercise("Preacher Curl", "Use rope to curl", "DoCWeUBA0Gs"));

        rvLayoutManager = new LinearLayoutManager(mContext);
        rvAdapter = new ExercisesAdapter(armWorkout);
        rvExercises.setLayoutManager(rvLayoutManager);
        rvExercises.setAdapter(rvAdapter);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder>{

        Workout currWorkout;

        public ExercisesAdapter(Workout workout){
            this.currWorkout = workout;
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

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                //setup number pickers
                npWeight.setMinValue(0);
                npWeight.setMaxValue(200);
                npSets.setMinValue(0);
                npSets.setMaxValue(5);
                npSets.setValue(3);
                npReps.setMinValue(0);
                npReps.setMaxValue(50);
                npReps.setValue(10);

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
            final View v = holder.itemView;
            holder.tvName.setText(currWorkout.getExercises().get(position).getExerciseName());

            holder.bMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExerciseDetailFragment detailFragment = new ExerciseDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("exercise", currWorkout.getExercises().get(pos));
                    detailFragment.setArguments(bundle);
                    //replace fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, detailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return currWorkout.getExercises().size();
        }
    }
}
