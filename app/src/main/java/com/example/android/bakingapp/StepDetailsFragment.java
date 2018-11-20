package com.example.android.bakingapp;


import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.handlers.ExoPlayerVideoHandler;
import com.example.android.bakingapp.models.Steps;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsFragment extends Fragment {

    private Steps[] step;
    private int position;
    private String videoUrl = null;
    private String thumbnailURL = null;
    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();

    @BindView(R.id.step_description)
    TextView step_description;

    @BindView(R.id.step_short_description)
    TextView step_short_description;

    @BindView(R.id.exoPlayerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.iv_step_image)
    ImageView iv_step_image;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        ButterKnife.bind(this, rootView);

        step_short_description.setText(step[position].getShortDescription());

        step_description.setText(step[position].getDescription());

        // Initialize the player view
        if (!step[position].getVideoURL().isEmpty() && step[position].getVideoURL() != null){
            mPlayerView.setVisibility(View.VISIBLE);

            videoUrl = step[position].getVideoURL();

        } else {
            iv_step_image.setVisibility(View.VISIBLE);

            thumbnailURL = step[position].getThumbnailURL();
        }

        return rootView;
    }

    public void setStepData(Steps[] step) {
        this.step = step;
    }

    public void setPosition(int thePosition) {
        this.position = thePosition;
    }

    @SuppressLint("Assert")
    @Override
    public void onResume() {
        super.onResume();

        if(videoUrl != null){
            ExoPlayerVideoHandler.getInstance()
                    .prepareExoPlayerForUri(getContext(),
                            Uri.parse(videoUrl), mPlayerView);
            ExoPlayerVideoHandler.getInstance().goToForeground();
        }else{
            if (thumbnailURL.isEmpty()){
                iv_step_image.setBackgroundResource(R.drawable.no_video);
            } else {
                Picasso.with(getContext())
                        .load(step[position].getThumbnailURL())
                        .placeholder(R.drawable.no_video)
                        .error(R.drawable.no_video)
                        .into(iv_step_image);
            }
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        ExoPlayerVideoHandler.getInstance().goToBackground();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
    }
}
