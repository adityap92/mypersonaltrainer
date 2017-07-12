package io.mypersonaltrainer.mypersonaltrainer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by aditya on 7/10/17.
 */

public class WorkoutFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.bNextDetails)
    Button button;
    @BindView(R.id.rvExercises)
    RecyclerView rvExercises;
    RecyclerView.LayoutManager rvLayoutManager;
    RecyclerView.Adapter rvAdapter;
    Context mContext;

    public WorkoutFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();

        //rvLayoutManager = new LinearLayoutManager(mContext);
        //rvAdapter =
        //rvExercises.setLayoutManager(rvLayoutManager);
        //rvExercises.setAdapter(rvAdapter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExerciseDetailFragment detailFragment = new ExerciseDetailFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.container, detailFragment)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class ExercisesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
