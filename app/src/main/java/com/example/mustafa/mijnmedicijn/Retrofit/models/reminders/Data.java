package com.example.mustafa.mijnmedicijn.Retrofit.models.reminders;

import com.google.gson.annotations.SerializedName;

public class Data{
	@SerializedName("id")
	private int id;

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

	@SerializedName("medicine_name")
	private String medicineName;

	public Data(int id, String reminderTime, String doseUnit, String updatedAt, int userId, String doseQuantity, String createdAt, String reminderRepeatInfo, String medicineName) {
		this.id = id;
		this.reminderTime = reminderTime;
		this.doseUnit = doseUnit;
		this.updatedAt = updatedAt;
		this.userId = userId;
		this.doseQuantity = doseQuantity;
		this.createdAt = createdAt;
		this.reminderRepeatInfo = reminderRepeatInfo;
		this.medicineName = medicineName;
	}

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