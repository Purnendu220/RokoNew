package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("status_code")
    private String status;
    @SerializedName("data")
    private UserDataModel data;

    public String getStatus() { return status; }

    public UserDataModel getData() { return data; }
}
