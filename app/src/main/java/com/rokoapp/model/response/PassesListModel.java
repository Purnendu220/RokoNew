package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassesListModel {
    @SerializedName("status_code")
    private String status_code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<PassModel> data;

    public String getStatus_code() { return status_code; }

    public String getMessage() { return message; }

    public List<PassModel> getData() { return data; }
}
