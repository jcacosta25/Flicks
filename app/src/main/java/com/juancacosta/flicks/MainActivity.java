package com.juancacosta.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juancacosta.flicks.interfaces.MovieAdapter;
import com.juancacosta.flicks.models.Movie;
import com.juancacosta.flicks.models.MovieResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private List<Movie> movies;
    private MovieAdapter adapter;
    private ListView lvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        lvMovies = (ListView) findViewById(R.id.lv_movies);
        setSupportActionBar(toolbar);
        movies = new ArrayList<>();


        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=aa94b3c2c71ca34288378f22d536ab1f";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new GsonBuilder().create();
                MovieResponse movieResponse = gson.fromJson(String.valueOf(response), MovieResponse.class);
                movies = movieResponse.getResults();
                adapter = new MovieAdapter(getApplicationContext(),movies);
                lvMovies.setAdapter(adapter);
                Log.d("onSuccess: ",response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
