package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class BookingModel {
    @SerializedName("status_code")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private NewBookingModel data;


    public String getStatus() { return status; }

    public String getMessage() { return message; }

    public NewBookingModel getData() { return data; }
}
