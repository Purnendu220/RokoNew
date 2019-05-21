package com.rokoapp.model.request;

public class OnBoardingRequest {
    private String qr_code;
    private String user_id;

    public OnBoardingRequest(String qr_code, String user_id) {
        this.qr_code = qr_code;
        this.user_id = user_id;
    }
}
