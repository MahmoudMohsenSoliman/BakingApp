package com.mahmoud.bakingapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mahmoud.bakingapp.Model.RecipeInstruction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepFragment extends Fragment {

    private static final String SELECTED_POSITION = "exoplayer position" ;
    private static final String PLAY_WHEN_READY = "exoplayer play when ready";
    private static final String WINDOW_POSITION = "window_position";
    private List<RecipeInstruction> mInstructions;
    private int selected_position ;
    private boolean playWhenReady = true;
    private long playbackPosition ;


    @BindView(R.id.next_step_btn)
    Button nextButton;
    @BindView(R.id.prev_step_btn)
    Button prevButton;
    @BindView(R.id.inst_title_text_view)
    TextView titleTextView;
    @BindView(R.id.inst_desc_text_view)
    TextView descTextView;
    @BindView(R.id.thumbnail_img_view)
    ImageView thumbnail;
    @BindView(R.id.media_player_view)
    SimpleExoPlayerView mExoPlayerView;

    private SimpleExoPlayer mExoPlayer;

    boolean twoPane = false;
    private int currentWindow;


    public RecipeStepFragment() {

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null)
        {
            playbackPosition = savedInstanceState.getLong(SELECTED_POSITION);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(WINDOW_POSITION);
            mInstructions = savedInstanceState.getParcelableArrayList(RecipeInfoFragment.EXTRA_INSTRUCTION);
            selected_position = savedInstanceState.getInt(RecipeInfoFragment.EXTRA_SELECTED_INSTRUCTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this,rootView);

        nextButton.setOnClickListener(v -> {

            selected_position++;

            if(twoPane)
            {

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(RecipeInfoFragment.EXTRA_INSTRUCTION, (ArrayList<? extends Parcelable>) mInstructions);
                bundle.putInt(RecipeInfoFragment.EXTRA_SELECTED_INSTRUCTION,selected_position);
                bundle.putBoolean("twoPane",twoPane);


                    RecipeStepFragment stepFragment = new RecipeStepFragment();
                    stepFragment.setArguments(bundle);
                    stepFragment.setRetainInstance(true);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.recipe_inst_container,stepFragment,"step").commit();


            }
            else{
                Intent intent = new Intent(getActivity(),RecipeStepActivity.class);
                intent.putParcelableArrayListExtra(RecipeInfoFragment.EXTRA_INSTRUCTION, (ArrayList<? extends Parcelable>)mInstructions);
                intent.putExtra(RecipeInfoFragment.EXTRA_SELECTED_INSTRUCTION,selected_position);
                getActivity().finish();
                getActivity().startActivity(intent);
            }

            });
        prevButton.setOnClickListener(v -> {
            selected_position--;

            if(twoPane)
            {

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(RecipeInfoFragment.EXTRA_INSTRUCTION, (ArrayList<? extends Parcelable>) mInstructions);
                bundle.putInt(RecipeInfoFragment.EXTRA_SELECTED_INSTRUCTION,selected_position);
                bundle.putBoolean("twoPane",twoPane);


                    RecipeStepFragment stepFragment = new RecipeStepFragment();
                    stepFragment.setArguments(bundle);
                    stepFragment.setRetainInstance(true);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.recipe_inst_container,stepFragment,"step").commit();

            }
            else{
                Intent intent = new Intent(getActivity(),RecipeStepActivity.class);
                intent.putParcelableArrayListExtra(RecipeInfoFragment.EXTRA_INSTRUCTION, (ArrayList<? extends Parcelable>)mInstructions);
                intent.putExtra(RecipeInfoFragment.EXTRA_SELECTED_INSTRUCTION,selected_position);
                getActivity().finish();
                getActivity().startActivity(intent);
            }
        });






        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();

        if(!getActivity().getIntent().hasExtra(RecipeInfoFragment.EXTRA_INSTRUCTION))
        {
            Bundle bundle = getArguments();
            twoPane = bundle.getBoolean("twoPane");
            mInstructions = bundle.getParcelableArrayList(RecipeInfoFragment.EXTRA_INSTRUCTION);
            selected_position = bundle.getInt(RecipeInfoFragment.EXTRA_SELECTED_INSTRUCTION);
        }
        else{
            Intent intent = getActivity().getIntent();
            mInstructions = intent.getParcelableArrayListExtra(RecipeInfoFragment.EXTRA_INSTRUCTION);
            selected_position =  intent.getIntExtra(RecipeInfoFragment.EXTRA_SELECTED_INSTRUCTION,-1);
        }


        if(mInstructions!=null)
        {
            RecipeInstruction instruction = mInstructions.get(selected_position);
            populatePlayer(instruction);
            titleTextView.setText(instruction.getTitle());
            descTextView.setText(instruction.getDescription());
            getActivity().setTitle(instruction.getTitle());

            int size = mInstructions.size();

            if(selected_position==size-1)
            {
                nextButton.setVisibility(View.INVISIBLE);
                prevButton.setVisibility(View.VISIBLE);
            }
            else if (selected_position == 0)
            {
                prevButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);
            }
            else {
                nextButton.setVisibility(View.VISIBLE);
                prevButton.setVisibility(View.VISIBLE);
            }

        }



    }

    private void initPlayer(Uri mediaUri){

        if (mExoPlayer==null){

            mExoPlayerView.setVisibility(View.VISIBLE);


            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            mExoPlayer.prepare(mediaSource);
            mExoPlayerView.setPlayer(mExoPlayer);


            mExoPlayer.seekTo(playbackPosition);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null){
            playbackPosition = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }
    private void releasePlayer() {
        if (mExoPlayer != null) {
            playbackPosition = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void populatePlayer(RecipeInstruction instruction)
    {
        if (!instruction.getVideoUrl().isEmpty())
        {
            mExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            initPlayer(Uri.parse(instruction.getVideoUrl()));
        }


        else if (!instruction.getThumbnailUrl().isEmpty()) {

            mExoPlayerView.setVisibility(View.GONE);
            thumbnail.setVisibility(View.VISIBLE);
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorAccent));
            circularProgressDrawable.start();


            Glide.with(getContext())
                    .load(instruction.getThumbnailUrl()).override(344,194)
                    .placeholder(circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(thumbnail);
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SELECTED_POSITION, playbackPosition);
        outState.putBoolean(PLAY_WHEN_READY , playWhenReady);
        outState.putInt(WINDOW_POSITION,currentWindow);
        outState.putParcelableArrayList(RecipeInfoFragment.EXTRA_INSTRUCTION, (ArrayList<? extends Parcelable>) mInstructions);
        outState.putInt(RecipeInfoFragment.EXTRA_SELECTED_INSTRUCTION,selected_position);

    }
}
