package io.mypersonaltrainer.mypersonaltrainer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by aditya on 7/10/17.
 */

public class PlannerFragment extends Fragment {

    @BindView(R.id.tvMonday)
    TextView tvMonday;
    @BindView(R.id.lvDaysOfWeek)
    ListView lvWeekDays;
    @BindView(R.id.tvPlannerDate)
    TextView tvPlannerDate;
    private Unbinder unbinder;
    Context mContext;
    Calendar cal;

    public PlannerFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext= getContext();
        cal = Calendar.getInstance();

        final String formattedDate = new SimpleDateFormat("MM/dd/yyyy").format(cal.getTime());
        tvPlannerDate.setText(formattedDate);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        List<String> arr = Arrays.asList(getResources().getStringArray(R.array.days_of_week));
        final int posInWeek = arr.indexOf(dayOfTheWeek);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.days_of_week)){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position,convertView,parent);

                if(position == posInWeek){
                    v.setBackgroundResource(R.color.colorAccent);
                }else
                    v.setBackgroundResource(android.R.color.white);
                return v;
            }
        };
        lvWeekDays.setAdapter(adapter);

        lvWeekDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                int diff = pos - posInWeek;
                cal.add(Calendar.DATE, diff);


                WorkoutFragment workoutFragment = new WorkoutFragment();
                Bundle bundle = new Bundle();
                bundle.putString("date", new SimpleDateFormat("MM/dd/yyyy").format(cal.getTime()));
                workoutFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, workoutFragment)
                        .addToBackStack(null)
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
