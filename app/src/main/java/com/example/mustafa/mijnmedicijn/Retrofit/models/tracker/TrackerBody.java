package com.example.mustafa.mijnmedicijn.Retrofit.models.tracker;

import com.google.gson.annotations.SerializedName;

public class TrackerBody{

	@SerializedName("date")
	private String date;

	@SerializedName("intake_quantity")
	private String intakeQuantity;

	@SerializedName("time")
	private String time;

	@SerializedName("medicine_name")
	private String medicineName;

	public TrackerBody(String date, String intakeQuantity, String time, String medicineName) {
		this.date = date;
		this.intakeQuantity = intakeQuantity;
		this.time = time;
		this.medicineName = medicineName;
	}

	public String getDate(){
		return date;
	}

	public String getIntakeQuantity(){
		return intakeQuantity;
	}

	public String getTime(){
		return time;
	}

	public String getMedicineName(){
		return medicineName;
	}
}