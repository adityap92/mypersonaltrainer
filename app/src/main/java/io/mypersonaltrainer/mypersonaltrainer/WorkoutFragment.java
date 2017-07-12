package io.mypersonaltrainer.mypersonaltrainer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

    public WorkoutFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        unbinder = ButterKnife.bind(this, view);


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
}
