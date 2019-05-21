package com.rokoapp.model.request;

public class RouteFindRequest {
    private String origin_lat;
    private String origin_long;
    private String destination_lat;
    private String destination_long;
    private String distance;

    /*public RouteFindRequest(String origin_lat, String origin_long, String destination_lat, String destination_long) {
        this.origin_lat = origin_lat;
        this.origin_long = origin_long;
        this.destination_lat = destination_lat;
        this.destination_long = destination_long;
    }*/

    public RouteFindRequest(String origin_lat, String origin_long, String destination_lat, String destination_long, String distance) {
        this.origin_lat = origin_lat;
        this.origin_long = origin_long;
        this.destination_lat = destination_lat;
        this.destination_long = destination_long;
        this.distance = distance;
    }
}
