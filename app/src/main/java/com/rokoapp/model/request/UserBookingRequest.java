package com.rokoapp.model.request;

public class UserBookingRequest {
    private String booked_by;
    private String virtual_route_id;
    private String origin_station;
    private String destination_station;
    private String booking_date_time;

    public UserBookingRequest(String booked_by, String virtual_route_id, String origin_station, String destination_station, String booking_date_time) {
        this.booked_by = booked_by;
        this.virtual_route_id = virtual_route_id;
        this.origin_station = origin_station;
        this.destination_station = destination_station;
        this.booking_date_time = booking_date_time;
    }
}
