package com.rokoapp.mapUtils.responseModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapRes {
    private MapData location;
    private int originalIndex;
    private String placeId;

    public MapRes(MapData location) {
        this.location = location;
    }

    public MapData getLocation() { return location; }

    public int getOriginalIndex() { return originalIndex; }

    public String getPlaceId() { return placeId; }

    public class MapData {

        private double latitude;
        private double longitude;

        public MapData(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}
