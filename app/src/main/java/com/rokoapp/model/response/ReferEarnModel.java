package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class ReferEarnModel {

    @SerializedName("status_code")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private String data;

    public String getStatus() { return status; }

    public String getMessage() { return message; }

    public String getData() { return data; }
}
