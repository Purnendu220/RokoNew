package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RouteFinderModel implements Serializable {
    @SerializedName("origin_station_id")
    private String origin_station_id;
    @SerializedName("distance_destination")
    private String distanceDestination;
    @SerializedName("destination_time")
    private String destinationTime;
    @SerializedName("route_date_and_time")
    private String routeDateAndTime;
    @SerializedName("destination_station")
    private String destinationStation;
    @SerializedName("qr_code")
    private String QRCode;
    @SerializedName("distance_origin")
    private String distanceOrigin;
    @SerializedName("route_name")
    private String routeName;
    @SerializedName("route_id")
    private String routeId;
    @SerializedName("availabe_seats")
    private String availableSeats;
    @SerializedName("origin_time")
    private String originTime;
    @SerializedName("origin_station")
    private String originStation;
    @SerializedName("virtual_route_id")
    private String virtualRouteId;
    @SerializedName("total_seats")
    private String totalSeats;
    @SerializedName("destination_station_id")
    private String destinationStationId;

    public RouteFinderModel(String origin_station_id, String distanceDestination, String destinationTime, String routeDateAndTime, String destinationStation, String QRCode, String distanceOrigin, String routeName, String routeId, String availableSeats, String originTime, String originStation, String virtualRouteId, String totalSeats, String destinationStationId) {
        this.origin_station_id = origin_station_id;
        this.distanceDestination = distanceDestination;
        this.destinationTime = destinationTime;
        this.routeDateAndTime = routeDateAndTime;
        this.destinationStation = destinationStation;
        this.QRCode = QRCode;
        this.distanceOrigin = distanceOrigin;
        this.routeName = routeName;
        this.routeId = routeId;
        this.availableSeats = availableSeats;
        this.originTime = originTime;
        this.originStation = originStation;
        this.virtualRouteId = virtualRouteId;
        this.totalSeats = totalSeats;
        this.destinationStationId = destinationStationId;
    }

    /*public RouteFinderModel(String origin_station_id, String distanceDestination, String routeDateAndTime, String destinationStation, String distanceOrigin, String routeName, String routeId, String availableSeats, String originStation, String virtualRouteId, String totalSeats, String destinationStationId) {
        this.origin_station_id = origin_station_id;
        this.distanceDestination = distanceDestination;
        this.routeDateAndTime = routeDateAndTime;
        this.destinationStation = destinationStation;
        this.distanceOrigin = distanceOrigin;
        this.routeName = routeName;
        this.routeId = routeId;
        this.availableSeats = availableSeats;
        this.originStation = originStation;
        this.virtualRouteId = virtualRouteId;
        this.totalSeats = totalSeats;
        this.destinationStationId = destinationStationId;
    }*/

    public String getOrigin_station_id() {
        return origin_station_id;
    }

    public String getDistanceDestination() {
        return distanceDestination;
    }

    public String getRouteDateAndTime() {
        return routeDateAndTime;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public String getDistanceOrigin() {
        return distanceOrigin;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public String getOriginStation() {
        return originStation;
    }

    public String getVirtualRouteId() {
        return virtualRouteId;
    }

    public String getTotalSeats() {
        return totalSeats;
    }

    public String getDestinationStationId() {
        return destinationStationId;
    }

    public String getDestinationTime() { return destinationTime; }

    public String getQRCode() { return QRCode; }

    public String getOriginTime() { return originTime; }
}





    /*@SerializedName("origin_station_id")
    private String origin_station_id;
    @SerializedName("distance_destination")
    private String distance_destination;
    @SerializedName("route_id")
    private String route_id;
    @SerializedName("no_of_seats")
    private String no_of_seats;
    @SerializedName("destination_station")
    private String destination_station;
    @SerializedName("route_name")
    private String route_name;
    @SerializedName("distance_origin")
    private String distance_origin;
    @SerializedName("available_seats")
    private String available_seats;
    @SerializedName("route_start_time")
    private String route_start_time;
    @SerializedName("origin_station")
    private String origin_station;
    @SerializedName("destination_station_id")
    private String destination_station_id;

    public RouteFinderModel(String origin_station_id, String distance_destination, String route_id, String no_of_seats, String destination_station, String route_name, String distance_origin, String available_seats, String route_start_time, String origin_station, String destination_station_id) {
        this.origin_station_id = origin_station_id;
        this.distance_destination = distance_destination;
        this.route_id = route_id;
        this.no_of_seats = no_of_seats;
        this.destination_station = destination_station;
        this.route_name = route_name;
        this.distance_origin = distance_origin;
        this.available_seats = available_seats;
        this.route_start_time = route_start_time;
        this.origin_station = origin_station;
        this.destination_station_id = destination_station_id;
    }

    public String getOrigin_station_id() { return origin_station_id; }

    public String getDistance_destination() { return distance_destination; }

    public String getRoute_id() { return route_id; }

    public String getNo_of_seats() { return no_of_seats; }

    public String getDestination_station() { return destination_station; }

    public String getRoute_name() { return route_name; }

    public String getDistance_origin() { return distance_origin; }

    public String getAvailable_seats() { return available_seats; }

    public String getRoute_start_time() { return route_start_time; }

    public String getOrigin_station() { return origin_station; }

    public String getDestination_station_id() { return destination_station_id; }
}*/
