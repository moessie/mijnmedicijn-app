package com.example.mustafa.mijnmedicijn.Recycler.models;

public class IntakeHistoryRVModel {
    private String date;
    private String intakeHistory;

    public IntakeHistoryRVModel(String date, String intakeHistory) {
        this.date = date;
        this.intakeHistory = intakeHistory;
    }

    public String getDate() {
        return date;
    }

    public String getIntakeHistory() {
        return intakeHistory;
    }
}
