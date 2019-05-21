package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class UniqueEmailModel {
    @SerializedName("status_code")
    private String status;
    @SerializedName("message")
    private String message;

    public String getStatus() { return status; }

    public String getMessage() { return message; }
}
