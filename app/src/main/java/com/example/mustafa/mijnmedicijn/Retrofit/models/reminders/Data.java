package com.example.mustafa.mijnmedicijn.Retrofit.models.reminders;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("reminder_time")
	private String reminderTime;

	@SerializedName("dose_unit")
	private String doseUnit;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("dose_quantity")
	private String doseQuantity;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("reminder_repeat_info")
	private String reminderRepeatInfo;

	@SerializedName("id")
	private int id;

	@SerializedName("medicine_name")
	private String medicineName;

	public String getReminderTime(){
		return reminderTime;
	}

	public String getDoseUnit(){
		return doseUnit;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public int getUserId(){
		return userId;
	}

	public String getDoseQuantity(){
		return doseQuantity;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getReminderRepeatInfo(){
		return reminderRepeatInfo;
	}

	public int getId(){
		return id;
	}

	public String getMedicineName(){
		return medicineName;
	}
}