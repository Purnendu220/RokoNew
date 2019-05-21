package com.rokoapp.model.request;

public class PassAllocationReq {
    private String CUST_ID;
    private String ORDER_ID;
    private String PAYMENT_STATUS;
    private String PASS_ID;

    public PassAllocationReq(String CUST_ID, String ORDER_ID, String PAYMENT_STATUS, String PASS_ID) {
        this.CUST_ID = CUST_ID;
        this.ORDER_ID = ORDER_ID;
        this.PAYMENT_STATUS = PAYMENT_STATUS;
        this.PASS_ID = PASS_ID;
    }
}
