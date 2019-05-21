package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class ChecksumResponse {
    @SerializedName("MID")
    private String mId;
    @SerializedName("CHANNEL_ID")
    private String channelId;
    @SerializedName("INDUSTRY_TYPE_ID")
    private String industryTypeId;
    @SerializedName("CUST_ID")
    private String customerId;
    @SerializedName("MOBILE_NO")
    private String mobileNo;
    @SerializedName("EMAIL")
    private String email;
    @SerializedName("WEBSITE")
    private String website;
    @SerializedName("ORDER_ID")
    private String orderId;
    @SerializedName("CALL_BACK_URL")
    private String callbackUrl;
    @SerializedName("CHECKSUMHASH")
    private String checksumHash;
    @SerializedName("TXN_AMOUNT")
    private String txnAmount;

    public String getmId() { return mId; }

    public String getChannelId() { return channelId; }

    public String getIndustryTypeId() { return industryTypeId; }

    public String getCustomerId() { return customerId; }

    public String getMobileNo() { return mobileNo; }

    public String getEmail() { return email; }

    public String getWebsite() { return website; }

    public String getOrderId() { return orderId; }

    public String getCallbackUrl() { return callbackUrl; }

    public String getChecksumHash() { return checksumHash; }

    public String getTxnAmount() { return txnAmount; }

}
