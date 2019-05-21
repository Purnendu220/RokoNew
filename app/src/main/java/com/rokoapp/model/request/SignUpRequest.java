package com.rokoapp.model.request;

public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private String userType;
    private String gender;
    private String dob;
    private String phone_no;
    private String home_address;
    private String office_address;
    private String time_you_leave_home;
    private String time_you_leave_office;
    private String home_lat_long;
    private String office_lat_long;

   /* public SignUpRequest(String name, String email, String password, String userType, String gender, String dob, String phone_no) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.gender = gender;
        this.dob = dob;
        this.phone_no = phone_no;
    }*/

    public SignUpRequest(String name, String email, String password, String userType, String gender, String dob, String phone_no, String home_address, String office_address, String time_you_leave_home, String time_you_leave_office, String home_lat_long, String office_lat_long) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.gender = gender;
        this.dob = dob;
        this.phone_no = phone_no;
        this.home_address = home_address;
        this.office_address = office_address;
        this.time_you_leave_home = time_you_leave_home;
        this.time_you_leave_office = time_you_leave_office;
        this.home_lat_long = home_lat_long;
        this.office_lat_long = office_lat_long;
    }
}
