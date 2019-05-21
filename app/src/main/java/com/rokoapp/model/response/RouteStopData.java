package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteStopData {
    @SerializedName("status_code")
    private String status;
    @SerializedName("data")
    private List<MyRouteSubData> data;
    @SerializedName("message")
    private String message;

    public String getStatus() { return status; }

    public List<MyRouteSubData> getData() { return data; }

    public String getMessage() { return message; }

    /*public class Data{
        @SerializedName("latitude")
        private Double latitude;
        @SerializedName("id")
        private Integer id;
        @SerializedName("bus_id")
        private Integer bus_id;
        @SerializedName("bus_name")
        private String bus_name;
        @SerializedName("longitude")
        private Double longitude;


        public Data(Integer id, Integer bus_id, String bus_name, Double latitude, Double longitude) {
            this.id = id;
            this.bus_id = bus_id;
            this.bus_name = bus_name;
            this.latitude = latitude;
            this.longitude = longitude;
        }


        public Double getLatitude() { return latitude; }

        public Integer getId() { return id; }

        public Integer getBus_id() { return bus_id; }

        public String getBus_name() { return bus_name; }

        public Double getLongitude() { return longitude; }
    }*/
}
