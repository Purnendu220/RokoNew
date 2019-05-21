package com.rokoapp.model.request;

public class ChecksumRequest {
    private String MID;
    private String M_KEY;
    private String ORDER_ID;
    private String TXN_AMOUNT;
    private String CUST_ID;
    private String INDUSTRY_TYPE_ID;
    private String WEBSITE;
    private String CHANNEL_ID;
    private String CALL_BACK_URL;
    private String MOBILE_NO;
    private String EMAIL;

    public ChecksumRequest(String MID, String M_KEY, String ORDER_ID, String TXN_AMOUNT, String CUST_ID, String INDUSTRY_TYPE_ID, String WEBSITE, String CHANNEL_ID, String CALL_BACK_URL, String MOBILE_NO, String EMAIL) {
        this.MID = MID;
        this.M_KEY = M_KEY;
        this.ORDER_ID = ORDER_ID;
        this.TXN_AMOUNT = TXN_AMOUNT;
        this.CUST_ID = CUST_ID;
        this.INDUSTRY_TYPE_ID = INDUSTRY_TYPE_ID;
        this.WEBSITE = WEBSITE;
        this.CHANNEL_ID = CHANNEL_ID;
        this.CALL_BACK_URL = CALL_BACK_URL;
        this.MOBILE_NO = MOBILE_NO;
        this.EMAIL = EMAIL;
    }
}
