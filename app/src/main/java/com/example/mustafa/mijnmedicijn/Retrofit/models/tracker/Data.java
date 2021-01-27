package com.example.mustafa.mijnmedicijn.Retrofit.models.tracker;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("date")
	private String date;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("intake_quantity")
	private String intakeQuantity;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("time")
	private String time;

	@SerializedName("medicine_name")
	private String medicineName;

	public String getDate(){
		return date;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public int getUserId(){
		return userId;
	}

	public String getIntakeQuantity(){
		return intakeQuantity;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public String getTime(){
		return time;
	}

	public String getMedicineName(){
		return medicineName;
	}
}