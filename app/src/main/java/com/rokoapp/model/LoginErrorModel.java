package com.rokoapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginErrorModel {
    @SerializedName("status_code")
    private String status;
    @SerializedName("email")
    private List<String> email;

    public String getStatus() { return status; }

    public List<String> getEmail() { return email; }
}
