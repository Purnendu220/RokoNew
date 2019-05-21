package com.rokoapp.model;

public class FireBaseDbModel {
    private double altitude;
    private double latitude;
    private double longitude;

    public FireBaseDbModel(double altitude, double latitude, double longitude) {
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getAltitude() { return altitude; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }
}
