package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChecksumMain {
    @SerializedName("status_code")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ChecksumResponse data;

    public String getStatus() { return status; }

    public String getMessage() { return message; }

    public ChecksumResponse getData() { return data; }
}
