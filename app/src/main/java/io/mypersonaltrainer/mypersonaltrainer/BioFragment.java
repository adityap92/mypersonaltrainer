package io.mypersonaltrainer.mypersonaltrainer;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.mypersonaltrainer.mypersonaltrainer.data.DBContract;

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
    @BindView(R.id.etFullName)
    TextView etFullName;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    Context mContext;
    String gender, tActivityLevel;

    public BioFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bio, container, false);
        unbinder = ButterKnife.bind(this, view);

        mContext = getContext();
        Bundle bundle = getArguments();

        if(bundle!=null){
            etFullName.setText(bundle.getString("full_name", ""));
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                switch(i){
                    case R.id.rbMale:
                        gender = "M";
                        break;
                    case R.id.rbFemale:
                        gender = "F";
                        break;
                }
            }
        });

        //format NumberPickers for height and weight
        npBioWeight.setMaxValue(400);
        npBioWeight.setMinValue(0);
        npBioWeight.setValue(150);
        npBioHeight.setMaxValue(84);
        npBioHeight.setMinValue(48);
        npBioHeight.setValue(65);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.arr_activity_level, android.R.layout.simple_spinner_dropdown_item);
        activityLevel.setAdapter(adapter);
        activityLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tActivityLevel = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues cv = new ContentValues();
                cv.put(DBContract.UsersEntry.COLUMN_USER_NAME,etFullName.getText().toString());
                cv.put(DBContract.UsersEntry.COLUMN_USER_GENDER,gender);
                cv.put(DBContract.UsersEntry.COLUMN_USER_WEIGHT, npBioWeight.getValue());
                cv.put(DBContract.UsersEntry.COLUMN_USER_HEIGHT, npBioHeight.getValue());
                cv.put(DBContract.UsersEntry.COLUMN_USER_ACTIVITY_LEVEL, tActivityLevel);
                Uri uri = mContext.getContentResolver().insert(
                        DBContract.UsersEntry.CONTENT_URI,
                        cv);

                if(uri!=null){
                    PlannerFragment plannerFragment = new PlannerFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, plannerFragment)
                            .commit();
                }

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
