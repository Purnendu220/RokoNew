package com.rokoapp.model.request;

public class LoginRequest {
    private String email;
    private String password;
    private String user_type;

    public LoginRequest(String email, String password, String user_type) {
        this.email = email;
        this.password = password;
        this.user_type = user_type;
    }
}
