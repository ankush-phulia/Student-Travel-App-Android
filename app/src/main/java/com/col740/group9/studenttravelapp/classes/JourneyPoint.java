package com.col740.group9.studenttravelapp.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class JourneyPoint {
    public String location_name;
    public String latitude;
    public String longitude;

    public JourneyPoint(JSONObject journey_point_json) throws JSONException {
        this.location_name = journey_point_json.getString("location_name");
        this.latitude = journey_point_json.getString("latitude");
        this.longitude = journey_point_json.getString("longitude");
    }

}
