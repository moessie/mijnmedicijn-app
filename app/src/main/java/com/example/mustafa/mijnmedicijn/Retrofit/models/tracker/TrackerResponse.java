package com.example.mustafa.mijnmedicijn.Retrofit.models.tracker;

import com.google.gson.annotations.SerializedName;

public class TrackerResponse{

	@SerializedName("data")
	private Data data;

	public Data getData(){
		return data;
	}
}