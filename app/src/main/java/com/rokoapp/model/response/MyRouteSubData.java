package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class MyRouteSubData {
    @SerializedName("id")
    private Integer id;
    @SerializedName("bus_id")
    private Integer bus_id;
    @SerializedName("bus_name")
    private String bus_name;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;

    public MyRouteSubData(Integer id, Integer bus_id, String bus_name, Double latitude, Double longitude) {
        this.id = id;
        this.bus_id = bus_id;
        this.bus_name = bus_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() { return id; }

    public Integer getBus_id() { return bus_id; }

    public String getBus_name() { return bus_name; }

    public Double getLatitude() { return latitude; }

    public Double getLongitude() { return longitude; }
}
