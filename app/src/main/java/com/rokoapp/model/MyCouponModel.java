package com.rokoapp.model;

public class MyCouponModel {
    private String id;
    private String couponName;
    private String couponDesc;
    private String couponValidity;

    public MyCouponModel(String id, String couponName, String couponDesc, String couponValidity) {
        this.id = id;
        this.couponName = couponName;
        this.couponDesc = couponDesc;
        this.couponValidity = couponValidity;
    }

    public String getId() { return id; }

    public String getCouponName() { return couponName; }

    public String getCouponDesc() { return couponDesc; }

    public String getCouponValidity() { return couponValidity; }
}
