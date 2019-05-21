package com.rokoapp.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TripHistorySubModel implements Serializable {
   /* @SerializedName("id")
    private String id;
    @SerializedName("route_name")
    private String routeName;
    @SerializedName("name")
    private String name;
    @SerializedName("destination_station_name")
    private String destinationStationName;
    @SerializedName("origin_station_name")
    private String originStationName;
    @SerializedName("booking_date_and_time")
    private String bookingDateTime;
    @SerializedName("attended")
    private String attended;
    @SerializedName("attended_date_time")
    private String attendedDateTime;
    @SerializedName("route_date_time")
    private String routeDateTime;*/





    @SerializedName("id")
    private String id;
    @SerializedName("route_name")
    private String routeName;
    @SerializedName("name")
    private String name;
    @SerializedName("destination_station_name")
    private String destinationStationName;
    @SerializedName("origin_station_name")
    private String originStationName;
    @SerializedName("booking_date_and_time")
    private String bookingDateTime;
    @SerializedName("attended_date_time")
    private String attendedDateTime;
    @SerializedName("route_date_time")
    private String routeDateTime;
    @SerializedName("route_id")
    private String routeId;
    @SerializedName("virtual_route_id")
    private String virtualRouteId;
    @SerializedName("attended")
    @Expose
    private String attended;
    @SerializedName("origin_station_lat")
    @Expose
    private double origin_station_lat;
    @SerializedName("destination_station_long")
    @Expose
    private double destination_station_long;
    @SerializedName("destination_station_lat")
    @Expose
    private double destination_station_lat;
    @SerializedName("origin_station_long")
    @Expose
    private double origin_station_long;
    @SerializedName("destination_station")
    @Expose
    private int destination_station;
    @SerializedName("origin_station")
    @Expose
    private int origin_station;


    public TripHistorySubModel(String id, String routeName, String name, String destinationStationName, String originStationName, String bookingDateTime, String attended, String attendedDateTime, String routeDateTime) {
        this.id = id;
        this.routeName = routeName;
        this.name = name;
        this.destinationStationName = destinationStationName;
        this.originStationName = originStationName;
        this.bookingDateTime = bookingDateTime;
        this.attended = attended;
        this.attendedDateTime = attendedDateTime;
        this.routeDateTime = routeDateTime;
    }

    public TripHistorySubModel(String id, String routeName, String name, String destinationStationName, String originStationName, String bookingDateTime, String attendedDateTime, String routeDateTime, String routeId, String virtualRouteId, String attended, double origin_station_lat, double destination_station_long, double destination_station_lat, double origin_station_long, int destination_station, int origin_station) {
        this.id = id;
        this.routeName = routeName;
        this.name = name;
        this.destinationStationName = destinationStationName;
        this.originStationName = originStationName;
        this.bookingDateTime = bookingDateTime;
        this.attendedDateTime = attendedDateTime;
        this.routeDateTime = routeDateTime;
        this.routeId = routeId;
        this.virtualRouteId = virtualRouteId;
        this.attended = attended;
        this.origin_station_lat = origin_station_lat;
        this.destination_station_long = destination_station_long;
        this.destination_station_lat = destination_station_lat;
        this.origin_station_long = origin_station_long;
        this.destination_station = destination_station;
        this.origin_station = origin_station;
    }

    public String getId() { return id; }

    public String getRouteName() { return routeName; }

    public String getName() { return name; }

    public String getDestinationStationName() { return destinationStationName; }

    public String getOriginStationName() { return originStationName; }

    public String getBookingDateTime() { return bookingDateTime; }

    public String getAttended() { return attended; }

    public String getAttendedDateTime() { return attendedDateTime; }

    public String getRouteDateTime() { return routeDateTime; }

    public String getRouteId() { return routeId; }

    public String getVirtualRouteId() { return virtualRouteId; }

    public double getOrigin_station_lat() { return origin_station_lat; }

    public double getDestination_station_long() { return destination_station_long; }

    public double getDestination_station_lat() { return destination_station_lat; }

    public double getOrigin_station_long() { return origin_station_long; }

    public int getDestination_station() { return destination_station; }

    public int getOrigin_station() { return origin_station; }
}
