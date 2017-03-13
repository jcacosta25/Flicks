package com.juancacosta.flicks.interfaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.juancacosta.flicks.R;
import com.juancacosta.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Juan C. Acosta on 3/9/2017.
 */

public class MovieDetailDialogFragment extends DialogFragment {
    private YouTubePlayer youTubePlayer;
    private double ratingvote;
    private String video;
    public static boolean destroy = true;

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
            if(ratingvote > 5){
                destroy = false;
                youTubePlayer.pause();
                youTubePlayer.setFullscreen(false);
                Intent intent = new Intent(getContext(),TrailerActivity.class);
                intent.putExtra("video",video);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };
    public MovieDetailDialogFragment(){

    }

    public static MovieDetailDialogFragment newInstance(Movie movie,String video){
        MovieDetailDialogFragment fragment = new MovieDetailDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("video",video);
        bundle.putString("title",movie.getTitle());
        bundle.putString("overview",movie.getOverview());
        bundle.putString("release","Release Date: "+movie.getReleaseDate());
        bundle.putDouble("rating",movie.getVoteAverage());
        bundle.putBoolean("big_screen",movie.getVoteAverage() > 5);
        bundle.putString("backdrop",movie.getBackdropPath());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_detail_dialog_fragment,container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title =(TextView) view.findViewById(R.id.tv_movie_title);
        TextView release = (TextView) view.findViewById(R.id.tv_movie_release);
        TextView overview = (TextView) view.findViewById(R.id.tv_movie_overview);
        RatingBar rating = (RatingBar) view.findViewById(R.id.rate_movie);
        title.setText(getArguments().getString("title"));
        release.setText(getArguments().getString("release"));
        overview.setText(getArguments().getString("overview"));
        ratingvote = getArguments().getDouble("rating");
        video = getArguments().getString("video");
        rating.setRating((float) ratingvote/2);

        if(video == null){
            view.findViewById(R.id.view_backdrop).setVisibility(View.VISIBLE);
            view.findViewById(R.id.video_movie_trailer_fragment).setVisibility(View.GONE);
            ImageView poster =(ImageView) view.findViewById(R.id.iv_movie_poster);
            Picasso.with(getContext()).load(getArguments().getString("backdrop")).fit().centerCrop()
                    .transform(new RoundedCornersTransformation(10, 10))
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.place_holder)
                    .into(poster);
        } else {
            YouTubePlayerSupportFragment playerView = (YouTubePlayerSupportFragment)
                    getFragmentManager().findFragmentById(R.id.video_movie_trailer_fragment);
            playerView.initialize(getResources().getString(R.string.youtube_api),
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer yp, boolean b) {
                            youTubePlayer = yp;
                            youTubePlayer.cueVideo(video);
                            youTubePlayer.setPlaybackEventListener(playbackEventListener);
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        destroy= true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(destroy) {
            YouTubePlayerSupportFragment fragment = (YouTubePlayerSupportFragment)
                    getFragmentManager().findFragmentById(R.id.video_movie_trailer_fragment);
            if(fragment != null){
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }

    }


}
