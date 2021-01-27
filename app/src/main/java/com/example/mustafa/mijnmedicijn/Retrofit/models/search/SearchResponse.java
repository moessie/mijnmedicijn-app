package com.example.mustafa.mijnmedicijn.Retrofit.models.search;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SearchResponse {

	@SerializedName("data")
	private List<DataItem> data;

	public List<DataItem> getData(){
		return data;
	}
}