package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripsHistoryModel {
    @SerializedName("status_code")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<TripHistorySubModel> data;

    public String getStatus() { return status; }

    public String getMessage() { return message; }

    public List<TripHistorySubModel> getData() { return data; }
}
