package com.juancacosta.flicks.interfaces;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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

public class MovieAdapter extends ArrayAdapter<Movie>{

    public MovieAdapter(Context context, List<Movie> movies){
        super(context,0,movies);
    }

    class ViewHolder{
        @BindView(R.id.tv_movie_title) TextView tvTitle;
        @BindView(R.id.tv_movie_overview) TextView tvOverview;
        @BindView(R.id.iv_movie_poster) ImageView ivPoster;
        ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.movie_view_element,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Display display = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();
        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90
                || orientation == Surface.ROTATION_270) {
            Picasso.with(getContext()).load(movie.getBackdropPath()).fit().centerCrop()
                    .transform(new RoundedCornersTransformation(10,10))
                    .placeholder(R.drawable.place_holder_backdrop)
                    .error(R.drawable.place_holder_backdrop)
                    .into(viewHolder.ivPoster);
        } else {
            Picasso.with(getContext()).load(movie.getPosterPath()).fit().centerCrop()
                    .transform(new RoundedCornersTransformation(10,10))
                    .placeholder(R.drawable.place_holder_poster)
                    .error(R.drawable.place_holder_poster)
                    .into(viewHolder.ivPoster);
        }

        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        return convertView;
    }
}