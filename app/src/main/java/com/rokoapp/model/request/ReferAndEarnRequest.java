package com.rokoapp.model.request;

public class ReferAndEarnRequest {
    private String referral_code;
    private String user_id;

    public ReferAndEarnRequest(String referral_code, String user_id) {
        this.referral_code = referral_code;
        this.user_id = user_id;
    }
}
