package com.col740.group9.studenttravelapp.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationPoint {
    public int id;
    public String location_name;
    public String location_type;
    public String latitude;
    public String longitude;
    public String rating;

    public LocationPoint(JSONObject journey_point_json) throws JSONException {
        this.id = journey_point_json.getInt("id");
        this.location_name = journey_point_json.getString("location_name");
        this.location_type = journey_point_json.getString("location_type");
        this.latitude = journey_point_json.getString("latitude");
        this.longitude = journey_point_json.getString("longitude");
        this.rating = journey_point_json.getString("rating");
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("location_name", this.location_name);
        json.put("location_type", this.location_type);
        json.put("latitude", this.latitude);
        json.put("longitude", this.longitude);
        json.put("rating", this.rating);
        return json;
    }

}
