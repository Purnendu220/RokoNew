package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class SignUpModel {

    @SerializedName("status_code")
    private String status;
    @SerializedName("data")
    private UserSignUpDataModel data;

    public String getStatus() { return status; }

    public UserSignUpDataModel getData() { return data; }
}
