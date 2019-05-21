package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class UserSignUpDataModel {

    @SerializedName("name")
    private String name;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("gender")
    private String gender;
    @SerializedName("userType")
    private String userType;
    @SerializedName("home_address")
    private String homeAddress;
    @SerializedName("office_address")
    private String officeAddress;
    @SerializedName("time_you_leave_home")
    private String homeLeavingTime;
    @SerializedName("time_you_leave_office")
    private String officeLeavingTime;
    @SerializedName("home_lat_long")
    private String homeLatLong;
    @SerializedName("office_lat_long")
    private String officeLatLong;
    @SerializedName("dob")
    private String dob;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getUserType() {
        return userType;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public String getHomeLeavingTime() {
        return homeLeavingTime;
    }

    public String getOfficeLeavingTime() {
        return officeLeavingTime;
    }

    public String getHomeLatLong() {
        return homeLatLong;
    }

    public String getOfficeLatLong() {
        return officeLatLong;
    }

    public String getDob() {
        return dob;
    }
}
