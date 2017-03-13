package com.juancacosta.flicks.interfaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.juancacosta.flicks.R;
import com.juancacosta.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by Juan C. Acosta on 3/8/2017.
 * Adapter for Items Response
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> movies;
    private Context context;
    private final int NORMAL = 0, RATING = 1;
    private OnItemClickListener listener;


    public MovieAdapter(List<Movie> movies, Context context, OnItemClickListener listener) {
        this.movies = movies;
        this.context = context;
        this.listener = listener;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView;
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case NORMAL:
                contactView = inflater.inflate(R.layout.movie_view_element, parent, false);
                viewHolder = new ViewHolder(contactView);
                break;
            case RATING:
                contactView = inflater.inflate(R.layout.movie_view_rating_element, parent, false);
                viewHolder = new ViewHolderPopular(contactView);
                break;
            default:
                contactView = inflater.inflate(R.layout.movie_view_element, parent, false);
                viewHolder = new ViewHolder(contactView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        final Movie movie = movies.get(position);
        switch (viewHolder.getItemViewType()) {
            case RATING:
                ViewHolderPopular popular = (ViewHolderPopular) viewHolder;
                ratingViewHolder(popular,movie);
                break;
            default:
                ViewHolder holder = (ViewHolder) viewHolder;
                defaultViewHolder(holder,movie);
                break;
        }


    }
    @Override
    public int getItemViewType(int position) {
        if (movies.get(position).getVoteAverage() > 5) {
            return RATING;
        }
        return NORMAL;
    }

    public void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private void defaultViewHolder(ViewHolder holder, Movie movie) {
        Display display = ((WindowManager) context.getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();
        int orientation = display.getRotation();
        String poster = movie.getPosterPath();
        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            poster = movie.getBackdropPath();
        }
        Picasso.with(context).load(poster).fit().centerCrop()
                .transform(new RoundedCornersTransformation(10, 10))
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .into(holder.ivPoster);

        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());
        holder.bind(movie,listener);
    }

    private void ratingViewHolder(ViewHolderPopular holder, final Movie movie) {

        Picasso.with(context).load(movie.getBackdropPath()).fit().centerCrop()
                .transform(new RoundedCornersTransformation(10, 10))
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .into(holder.ivPoster);

        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());
        /*holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTrailer(movie.getId());
            }
        });*/
        holder.bind(movie,listener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_movie_title)
        TextView tvTitle;
        @BindView(R.id.tv_movie_overview)
        TextView tvOverview;
        @BindView(R.id.iv_movie_poster)
        ImageView ivPoster;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Movie movie, final OnItemClickListener click){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.movieClick(movie);
                }
            });
        }
    }

    public class ViewHolderPopular extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_movie_title)
        TextView tvTitle;
        @BindView(R.id.tv_movie_overview)
        TextView tvOverview;
        @BindView(R.id.iv_movie_poster)
        ImageView ivPoster;

        public ViewHolderPopular(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Movie movie, final OnItemClickListener click){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.movieClick(movie);
                }
            });
        }
    }


    public interface OnItemClickListener{
        void movieClick(Movie movie);
    }

}