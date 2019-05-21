package com.rokoapp.model.request;

public class PostPayment {
    private String user_id;
    private String pass_id;
    private String payment_status;
    private String order_id;
    private int amount;

    public PostPayment(String user_id, String pass_id, String payment_status, String order_id, int amount) {
        this.user_id = user_id;
        this.pass_id = pass_id;
        this.payment_status = payment_status;
        this.order_id = order_id;
        this.amount = amount;
    }
}
