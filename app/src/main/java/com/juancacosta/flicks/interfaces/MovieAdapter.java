package com.juancacosta.flicks.interfaces;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.juancacosta.flicks.models.ResultsItem;

import java.util.List;

/**
 * Created by Juan C. Acosta on 3/8/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ResultsItem> movieList;

    //private final int BACKDROP = 0, POSTER = 1;

    public MovieAdapter(List<ResultsItem> movieList) {
        this.movieList = movieList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

}
