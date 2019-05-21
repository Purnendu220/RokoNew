package com.rokoapp.mapUtils.responseModel;

/*import com.oneclickaway.opensource.placeautocomplete.api.bean.places_response.MatchedSubstringsItem;
import com.oneclickaway.opensource.placeautocomplete.api.bean.places_response.StructuredFormatting;
import com.oneclickaway.opensource.placeautocomplete.api.bean.places_response.TermsItem;

import java.util.List;*/

public class SearchHistoryModel {
    /*private String reference;
    private List<String> types;
    private List<MatchedSubstringsItem> matched_substrings;
    private List<TermsItem> terms;
    private StructuredFormatting structured_formatting;
    private String description;
    private String id;
    private String place_id;*/

    private int id;
    private String placeName;
    private String formattedAddress;
    private double latitude;
    private double longitude;

    public SearchHistoryModel(String placeName, String formattedAddress, double latitude, double longitude) {
        this.placeName = placeName;
        this.formattedAddress = formattedAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public SearchHistoryModel(int id, String placeName, String formattedAddress, double latitude, double longitude) {
        this.id = id;
        this.placeName = placeName;
        this.formattedAddress = formattedAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() { return id; }

    public String getPlaceName() { return placeName; }

    public String getFormattedAddress() { return formattedAddress; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }
}
