package com.rokoapp.model.response;

import com.google.gson.annotations.SerializedName;

public class PassModel {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("no_of_rides")
    private String no_of_rides;
    @SerializedName("complimentry_ride")
    private String  complimentary_ride ;
    @SerializedName("validity_of_complimentry_ride")
    private String validUpTo;
    @SerializedName("cost")
    private int cost;
    @SerializedName("base_cost")
    private int baseCost;
    @SerializedName("tax")
    private int tax;
    @SerializedName("status")
    private boolean status;

    /*public PassModel(String id, String name, String description, String image_url, String no_of_rides, String complimentary_ride, String validUpTo, int cost, boolean status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.no_of_rides = no_of_rides;
        this.complimentary_ride = complimentary_ride;
        this.validUpTo = validUpTo;
        this.cost = cost;
        this.status = status;
    }*/

    public PassModel(String id, String name, String description, String image_url, String no_of_rides, String complimentary_ride, String validUpTo, int cost, int baseCost, int tax, boolean status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.no_of_rides = no_of_rides;
        this.complimentary_ride = complimentary_ride;
        this.validUpTo = validUpTo;
        this.cost = cost;
        this.baseCost = baseCost;
        this.tax = tax;
        this.status = status;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getImage_url() { return image_url; }

    public String getNo_of_rides() { return no_of_rides; }

    public String getComplimentary_ride() { return complimentary_ride; }

    public String getValidUpTo() { return validUpTo; }

    public int getCost() { return cost; }

    public int getBaseCost() { return baseCost; }

    public int getTax() { return tax; }

    public boolean  getStatus() { return status; }
}
