package io.mypersonaltrainer.mypersonaltrainer;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import io.mypersonaltrainer.mypersonaltrainer.utils.ExerciseHolder;

import static io.mypersonaltrainer.mypersonaltrainer.utils.ExerciseHolder.map;

/**
 * Created by aditya on 7/17/17.
 */

public class AddExerciseDialog extends DialogFragment {

    ExpandableListView elv;
    int selGroup, selChild;
    ExercisesAdapter adapter;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            elv = new ExpandableListView(getContext());
        }
        adapter = new ExercisesAdapter();
        elv.setAdapter(adapter);
        selGroup =-1;
        selChild=-1;


        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                selGroup = i;
                selChild = i1;
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(elv)
                .setTitle(getString(R.string.dialog_name))
                .setPositiveButton("Add Exercise", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                }).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(selGroup==-1||selChild==-1){
                            Snackbar snackbar = Snackbar.make(view, getString(R.string.select_workout),
                                    Snackbar.LENGTH_SHORT);
                            int snackbarTextId = android.support.design.R.id.snackbar_text;
                            View v = snackbar.getView();
                            TextView textView = (TextView) v.findViewById(snackbarTextId);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setTextColor(getResources().getColor(R.color.white, null));
                            }
                            snackbar.show();
                        }else{
                            WorkoutFragment.currWorkout.addExercise(ExerciseHolder.map.get(ExerciseHolder.exerciseNames.get(selGroup)).get(selChild));
                            WorkoutFragment.rvAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });



        return dialog;
    }


    public class ExercisesAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return ExerciseHolder.exerciseNames.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return map.get(ExerciseHolder.exerciseNames.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return ExerciseHolder.exerciseNames.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return null;
        }

        @Override
        public long getGroupId(int i) {
            return 0;
        }

        @Override
        public long getChildId(int i, int i1) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            if(view==null){
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.group_exercise, null);
            }

            TextView tv = (TextView) view.findViewById(R.id.tvGroupExercise);
            tv.setText(ExerciseHolder.exerciseNames.get(i));

            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
            if(view==null){
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.child_exercise, null);
            }

            TextView tv = (TextView) view.findViewById(R.id.tvChildExercise);
            tv.setText(map.get(ExerciseHolder.exerciseNames.get(groupPosition)).get(childPosition).getExerciseName());

            if(groupPosition == selGroup && childPosition == selChild) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));
                }
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    view.setBackgroundColor(getResources().getColor(R.color.white, null));
                }
            }

            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
