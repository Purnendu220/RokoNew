package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class NewBookingModel {
    @SerializedName("booking_detail")
    private BookingData bookingDetail ;
//    private TripHistorySubModel bookingDetail ;
    @SerializedName("user_data")
    private LoginModel userData = null;

    public BookingData getBookingDetail() { return bookingDetail; }
//    public TripHistorySubModel getBookingDetail() { return bookingDetail; }

    public LoginModel getUserData() { return userData; }
}
