package io.mypersonaltrainer.mypersonaltrainer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by aditya on 7/10/17.
 */

public class BioFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.bNext)
    Button bNext;
    @BindView(R.id.npBioWeight)
    NumberPicker npBioWeight;
    @BindView(R.id.npBioHeight)
    NumberPicker npBioHeight;
    @BindView(R.id.spinner)
    Spinner activityLevel;
    Context mContext;

    public BioFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bio, container, false);
        unbinder = ButterKnife.bind(this, view);

        mContext = getContext();

        npBioWeight.setMaxValue(400);
        npBioWeight.setMinValue(0);
        npBioWeight.setValue(150);

        npBioHeight.setMaxValue(84);
        npBioHeight.setMinValue(48);
        npBioHeight.setValue(65);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.arr_activity_level, android.R.layout.simple_spinner_dropdown_item);
        activityLevel.setAdapter(adapter);

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlannerFragment plannerFragment = new PlannerFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, plannerFragment)
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
