package com.juancacosta.flicks.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Dates{

	@SerializedName("maximum")
	@Expose
	private String maximum;

	@SerializedName("minimum")
	@Expose
	private String minimum;

	public void setMaximum(String maximum){
		this.maximum = maximum;
	}

	public String getMaximum(){
		return maximum;
	}

	public void setMinimum(String minimum){
		this.minimum = minimum;
	}

	public String getMinimum(){
		return minimum;
	}

}