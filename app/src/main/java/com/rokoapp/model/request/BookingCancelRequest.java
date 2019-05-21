package com.rokoapp.model.request;

public class BookingCancelRequest {
    private String booking_id;
    private String reason;

    public BookingCancelRequest(String booking_id, String reason) {
        this.booking_id = booking_id;
        this.reason = reason;
    }
}
