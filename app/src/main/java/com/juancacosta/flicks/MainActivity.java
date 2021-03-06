package com.juancacosta.flicks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.juancacosta.flicks.interfaces.MovieAdapter;
import com.juancacosta.flicks.interfaces.MovieDetailDialogFragment;
import com.juancacosta.flicks.models.Movie;
import com.juancacosta.flicks.models.MovieResponse;
import com.juancacosta.flicks.models.MovieTrailerResponse;
import com.juancacosta.flicks.utils.EndlessRecyclerViewScrollListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private List<Movie> movies;
    private MovieAdapter adapter;
    private RecyclerView lvMovies;
    private final Gson gson = new Gson();
    private int page = 1;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        lvMovies = (RecyclerView) findViewById(R.id.lv_movies);
        setSupportActionBar(toolbar);
        movies = new ArrayList<>();
        adapter = new MovieAdapter(movies, getApplicationContext(), new MovieAdapter.OnItemClickListener() {
            @Override
            public void movieClick(Movie movie) {
                getTrailer(movie);
            }
        });
        lvMovies.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        lvMovies.setLayoutManager(layoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getMovies(page+1);
            }
        };
        lvMovies.addOnScrollListener(scrollListener);
        getMovies(page);
    }

    private void getMovies(int page){
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=aa94b3c2c71ca34288378f22d536ab1f&page="+page;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    final String responseData = response.body().string();
                    MovieResponse movieResponse = gson.fromJson(responseData, MovieResponse.class);
                    movies = movieResponse.getResults();
                    MainActivity.this.getMainLooper();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addMovies(movies);
                        }
                    });
                }
            }
        });
    }

    public void getTrailer(final Movie movie) {
        String url = "https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=aa94b3c2c71ca34288378f22d536ab1f";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    MovieTrailerResponse trailer = gson.
                            fromJson(responseData, MovieTrailerResponse.class);
                    FragmentManager fm = getSupportFragmentManager();
                    MovieDetailDialogFragment movieDetailDialogFragment;
                    if(trailer.getResults().size() > 0) {
                        movieDetailDialogFragment = MovieDetailDialogFragment
                                .newInstance(movie,trailer.getResults().get(0).getKey());

                    }else {
                        movieDetailDialogFragment = MovieDetailDialogFragment
                                .newInstance(movie,null);
                    }
                    movieDetailDialogFragment.show(fm,"Movie");


                }
            }
        });
    }
}
