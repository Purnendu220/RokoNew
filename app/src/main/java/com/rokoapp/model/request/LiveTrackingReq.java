package com.rokoapp.model.request;

public class LiveTrackingReq {
    private String virtual_route_id;
    private String current_lat;
    private String current_long;

    public LiveTrackingReq(String virtual_route_id, String current_lat, String current_long) {
        this.virtual_route_id = virtual_route_id;
        this.current_lat = current_lat;
        this.current_long = current_long;
    }
}
