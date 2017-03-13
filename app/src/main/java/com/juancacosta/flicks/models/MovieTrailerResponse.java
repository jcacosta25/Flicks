package com.juancacosta.flicks.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailerResponse {

	@SerializedName("id")
	@Expose
	private int id;

	@SerializedName("results")
	@Expose
	private List<MovieTrailer> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<MovieTrailer> results){
		this.results = results;
	}

	public List<MovieTrailer> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"MovieTrailer{" + 
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}