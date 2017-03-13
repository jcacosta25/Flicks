package com.juancacosta.flicks.interfaces;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.juancacosta.flicks.R;

public class TrailerActivity extends YouTubeBaseActivity{
    private YouTubePlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);
        final String video = getIntent().getStringExtra("video");
        final YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.video_movie_trailer);
        playerView.initialize(getResources().getString(R.string.youtube_api)
                , new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider
                    , YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer;
                player.loadVideo(video);
                player.setFullscreen(true);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider
                    , YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.pause();
        MovieDetailDialogFragment.destroy = true;
        player.setFullscreen(false);
    }
}
