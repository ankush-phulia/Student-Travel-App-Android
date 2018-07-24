package com.col740.group9.studenttravelapp.classes;


import org.json.JSONException;
import org.json.JSONObject;

public class JourneyPoint {
    public String point_id;
    public LocationPoint location;
    public String transport;

    public JourneyPoint() {}

    public JourneyPoint(JSONObject journey_point_json) throws JSONException {
        this.point_id = journey_point_json.getString("point_id");
        this.location = new LocationPoint(journey_point_json.getJSONObject("location"));
        this.transport = journey_point_json.getString("transport");
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("point_id", this.point_id);
        json.put("location", this.location.toJson());
        json.put("transport", this.transport);
        return json;
    }
}
