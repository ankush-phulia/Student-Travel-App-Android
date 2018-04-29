package com.col740.group9.studenttravelapp.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class JourneyPoint {
    public String point_id;
    public String location;
    public String transport;
    //    public String longitude;
    //    public String latitude;

    public JourneyPoint(JSONObject journey_point_json) throws JSONException {
        this.point_id = journey_point_json.getString("point_id");
        this.location = journey_point_json.getString("location");
        this.transport = journey_point_json.getString("transport");
//        this.latitude = journey_point_json.getString("latitude");
//        this.longitude = journey_point_json.getString("longitude");
    }

}
