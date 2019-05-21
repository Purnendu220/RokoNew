package com.rokoapp.model.request;

public class PhoneDetailRequest {

    private String user_id;
    private String device_token;
    private String device_type;
    private String end_point_arn;
    private String os_version;
    private String app_version;
    private String device_name;
    private String loged_in_token;
    private String android_id;

    public PhoneDetailRequest(String user_id, String device_token, String device_type, String end_point_arn, String os_version, String app_version, String device_name, String loged_in_token, String android_id) {
        this.user_id = user_id;
        this.device_token = device_token;
        this.device_type = device_type;
        this.end_point_arn = end_point_arn;
        this.os_version = os_version;
        this.app_version = app_version;
        this.device_name = device_name;
        this.loged_in_token = loged_in_token;
        this.android_id = android_id;
    }
}
