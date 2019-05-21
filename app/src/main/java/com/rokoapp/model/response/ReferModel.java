package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class ReferModel {
    @SerializedName("android_url")
    private String androidUrl;
    @SerializedName("ios_url")
    private String iosUrl;
    @SerializedName("web_url")
    private String webUrl;
    @SerializedName("refer_and_code")
    private String referAndCode;

    public String getAndroidUrl() { return androidUrl; }

    public String getIosUrl() { return iosUrl; }

    public String getWebUrl() { return webUrl; }

    public String getReferAndCode() { return referAndCode; }
}
