package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class UserDataModel {


    @SerializedName("home_address")
    private String homeAddress;
    @SerializedName("userType")
    private String userType;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("office_address")
    private String officeAddress;
    @SerializedName("dob")
    private String dob;
    @SerializedName("gender")
    private String gender;
    @SerializedName("time_you_leave_office")
    private String officeLeavingTime;
    @SerializedName("office_lat_long")
    private String officeLatLong;
    @SerializedName("home_lat_long")
    private String homeLatLong;
    @SerializedName("time_you_leave_home")
    private String homeLeavingTime;
    @SerializedName("email")
    private String email;
    @SerializedName("phone_no")
    private String phone_no;
    @SerializedName("earned")
    private boolean earned;
    @SerializedName("refer_and_earn")
    private ReferModel referAndEarn;
    @SerializedName("user_pass_obj")
    private UserPassModel userPassDetail = null;


    public String getOfficeLeavingTime() { return officeLeavingTime; }

    public String getUserType() { return userType; }

    public String getHomeAddress() { return homeAddress; }

    public String getName() { return name; }

    public String getOfficeAddress() { return officeAddress; }

    public String getOfficeLatLong() { return officeLatLong; }

    public String getGender() { return gender; }

    public String getHomeLatLong() { return homeLatLong; }

    public String getEmail() { return email; }

    public String getHomeLeavingTime() { return homeLeavingTime; }

    public String getId() { return id; }

    public String getDob() { return dob; }

    public String getPhone_no() { return phone_no; }

    public boolean isEarned() { return earned; }

    public ReferModel getReferAndEarn() { return referAndEarn; }

    public UserPassModel getUserPassDetail() { return userPassDetail; }
}
