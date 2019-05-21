package com.rokoapp.model;

import com.google.gson.annotations.SerializedName;

public class BadRequestModel {
    @SerializedName("status_code")
    private String statusCode;
    @SerializedName("message")
    private String message;

    public String getStatusCode() { return statusCode; }

    public String getMessage() { return message; }
}
