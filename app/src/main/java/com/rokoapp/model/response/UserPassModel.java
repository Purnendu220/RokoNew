package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class UserPassModel {
    @SerializedName("complimentry_pass")
    private String complimentaryPass;
    @SerializedName("complimentry_pass_validity")
    private String complimentaryPassValidity;
    @SerializedName("total_pass")
    private String totalPass;

    public String getComplimentaryPass() { return complimentaryPass; }

    public String getComplimentaryPassValidity() { return complimentaryPassValidity; }

    public String getTotalPass() { return totalPass; }
}
