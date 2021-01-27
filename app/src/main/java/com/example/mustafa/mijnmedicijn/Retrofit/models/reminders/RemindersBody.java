package com.example.mustafa.mijnmedicijn.Retrofit.models.reminders;

import com.google.gson.annotations.SerializedName;

public class RemindersBody{

	@SerializedName("reminder_time")
	private String reminderTime;

	@SerializedName("dose_unit")
	private String doseUnit;

	@SerializedName("dose_quantity")
	private String doseQuantity;

	@SerializedName("reminder_repeat_info")
	private String reminderRepeatInfo;

	@SerializedName("medicine_name")
	private String medicineName;

	public RemindersBody(String reminderTime, String doseUnit, String doseQuantity, String reminderRepeatInfo, String medicineName) {
		this.reminderTime = reminderTime;
		this.doseUnit = doseUnit;
		this.doseQuantity = doseQuantity;
		this.reminderRepeatInfo = reminderRepeatInfo;
		this.medicineName = medicineName;
	}

	public String getReminderTime(){
		return reminderTime;
	}

	public String getDoseUnit(){
		return doseUnit;
	}

	public String getDoseQuantity(){
		return doseQuantity;
	}

	public String getReminderRepeatInfo(){
		return reminderRepeatInfo;
	}

	public String getMedicineName(){
		return medicineName;
	}
}