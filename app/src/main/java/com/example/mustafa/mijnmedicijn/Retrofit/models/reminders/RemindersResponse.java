package com.example.mustafa.mijnmedicijn.Retrofit.models.reminders;

import com.google.gson.annotations.SerializedName;

public class RemindersResponse{

	@SerializedName("data")
	private Data data;

	public Data getData(){
		return data;
	}
}