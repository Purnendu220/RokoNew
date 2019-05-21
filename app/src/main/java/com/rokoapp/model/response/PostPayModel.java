package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class PostPayModel {
    @SerializedName("status_code")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private UserDataModel data;

    public String getStatus() { return status; }

    public String getMessage() { return message; }

    public UserDataModel getData() { return data; }
}
