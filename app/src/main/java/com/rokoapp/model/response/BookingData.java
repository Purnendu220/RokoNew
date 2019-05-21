package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class BookingData {

    @SerializedName("id")
    private String id;
    @SerializedName("booked_by")
    private String booked_by;
    @SerializedName("virtual_route_id")
    private String virtualRouteId;
    @SerializedName("origin_station")
    private String origin_station;
    @SerializedName("destination_station_name")
    private String destination_station_name;
    @SerializedName("destination_station")
    private String destination_station;
    @SerializedName("origin_station_name")
    private String origin_station_name;
    @SerializedName("origin_station_latitude")
    private String origin_station_latitude;
    @SerializedName("origin_station_longitude")
    private String origin_station_longitude;
    @SerializedName("route_id")
    private String routeId;
    @SerializedName("name")
    private String name;
    @SerializedName("origin_station_status")
    private String origin_station_status;
    @SerializedName("destination_station_longitude")
    private String destination_station_longitude;
    @SerializedName("destination_station_latitude")
    private String destination_station_latitude;
    @SerializedName("route_name")
    private String routeName;
    @SerializedName("destination_station_status")
    private String destination_station_status;
    @SerializedName("booking_date_and_time")
    private String bookingDateTime;
    @SerializedName("route_date_time")
    private String routeDateTime;
    @SerializedName("route_origin_station_latitude")
    private String routeOriginStationLat;
    @SerializedName("route_origin_station_longitude")
    private String routeOriginStationLong;

    public BookingData(String id, String booked_by, String virtualRouteId, String origin_station, String destination_station_name, String destination_station, String origin_station_name, String origin_station_latitude, String origin_station_longitude, String routeId, String name, String origin_station_status, String destination_station_longitude, String destination_station_latitude, String routeName, String destination_station_status, String bookingDateTime, String routeDateTime, String routeOriginStationLat, String routeOriginStationLong) {
        this.id = id;
        this.booked_by = booked_by;
        this.virtualRouteId = virtualRouteId;
        this.origin_station = origin_station;
        this.destination_station_name = destination_station_name;
        this.destination_station = destination_station;
        this.origin_station_name = origin_station_name;
        this.origin_station_latitude = origin_station_latitude;
        this.origin_station_longitude = origin_station_longitude;
        this.routeId = routeId;
        this.name = name;
        this.origin_station_status = origin_station_status;
        this.destination_station_longitude = destination_station_longitude;
        this.destination_station_latitude = destination_station_latitude;
        this.routeName = routeName;
        this.destination_station_status = destination_station_status;
        this.bookingDateTime = bookingDateTime;
        this.routeDateTime = routeDateTime;
        this.routeOriginStationLat = routeOriginStationLat;
        this.routeOriginStationLong = routeOriginStationLong;
    }

    public String getId() { return id; }

    public String getBooked_by() { return booked_by; }

    public String getVirtualRouteId() { return virtualRouteId; }

    public String getOrigin_station() { return origin_station; }

    public String getDestination_station_name() { return destination_station_name; }

    public String getDestination_station() { return destination_station; }

    public String getOrigin_station_name() { return origin_station_name; }

    public String getOrigin_station_latitude() { return origin_station_latitude; }

    public String getOrigin_station_longitude() { return origin_station_longitude; }

    public String getRouteId() { return routeId; }

    public String getName() { return name; }

    public String getOrigin_station_status() { return origin_station_status; }

    public String getDestination_station_longitude() { return destination_station_longitude; }

    public String getDestination_station_latitude() { return destination_station_latitude; }

    public String getRouteName() { return routeName; }

    public String getDestination_station_status() { return destination_station_status; }

    public String getBookingDateTime() { return bookingDateTime; }

    public String getRouteDateTime() { return routeDateTime; }

    public String getRouteOriginStationLat() { return routeOriginStationLat; }

    public String getRouteOriginStationLong() { return routeOriginStationLong; }
}

