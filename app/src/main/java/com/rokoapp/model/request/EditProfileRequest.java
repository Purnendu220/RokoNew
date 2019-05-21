package com.rokoapp.model.request;

public class EditProfileRequest {

    private String userType;
    private String email;
    private String name;
    private String home_lat_long;
    private String time_you_leave_home;
    private String office_lat_long;
    private String time_you_leave_office;
    private String home_address;
    private String office_address;
    private String dob;
    private String gender;
    /*private String phone_no;*/

    /*public EditProfileRequest(String userType, String email, String name) {
        this.userType = userType;
        this.email = email;
        this.name = name;
    }*/

    public EditProfileRequest(String userType, String email, String name, String home_lat_long, String time_you_leave_home, String office_lat_long, String time_you_leave_office, String home_address, String office_address, String dob, String gender) {
        this.userType = userType;
        this.email = email;
        this.name = name;
        this.home_lat_long = home_lat_long;
        this.time_you_leave_home = time_you_leave_home;
        this.office_lat_long = office_lat_long;
        this.time_you_leave_office = time_you_leave_office;
        this.home_address = home_address;
        this.office_address = office_address;
        this.dob = dob;
        this.gender = gender;
    }
}
