package io.mypersonaltrainer.mypersonaltrainer;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mypersonaltrainer.mypersonaltrainer.utils.Exercise;

/**
 * Display Exercise Details
 * Created by aditya on 7/11/17.
 */

public class ExerciseDetailFragment extends Fragment {

    YouTubePlayerSupportFragment youtubeFragment;
    @BindView(R.id.tvInstructionsDetail)
    TextView tvInstructionsDetail;
    Exercise currExercise;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_exercise_detail, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currExercise = (Exercise) bundle.getSerializable("exercise");
        }
        //navigate back to previous fragment
        ((MainActivity) getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvInstructionsDetail.setText(currExercise.getExerciseInstruction());

        //initialize youtube fragment
        youtubeFragment = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtube_fragment);

        //check if video id provided
        if (!currExercise.getVidUrl().equals("")) {

            //initialize with api key
            youtubeFragment.initialize(getString(R.string.yt_api_key), new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    Log.d("YOUTUBEPLAYER FRAG", "SUCCESS");
                    if (!b) {
                        youTubePlayer.cueVideo(currExercise.getVidUrl());
                        youTubePlayer.play();
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.d("YOUTUBEPLAYER FRAG", "FAILURE");
                    Snackbar snackbar = Snackbar.make(getView(), getString(R.string.yt_fail),
                            Snackbar.LENGTH_LONG);
                    int snackbarTextId = android.support.design.R.id.snackbar_text;
                    View view = snackbar.getView();
                    TextView textView = (TextView) view.findViewById(snackbarTextId);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setTextColor(getResources().getColor(R.color.white, null));
                    }
                    snackbar.show();
                }
            });
        }

        return view;
    }
}
