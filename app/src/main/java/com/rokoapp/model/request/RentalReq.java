package com.rokoapp.model.request;

public class RentalReq {
    private String user;
    private String start_date_of_journey;
    private String end_date_of_journey;
    private String ride_start_point;
    private String ride_end_point;
    private String no_of_seats_required;
    private String bus_type;

    public RentalReq(String user, String start_date_of_journey, String end_date_of_journey, String ride_start_point, String ride_end_point, String no_of_seats_required, String bus_type) {
        this.user = user;
        this.start_date_of_journey = start_date_of_journey;
        this.end_date_of_journey = end_date_of_journey;
        this.ride_start_point = ride_start_point;
        this.ride_end_point = ride_end_point;
        this.no_of_seats_required = no_of_seats_required;
        this.bus_type = bus_type;
    }
}
