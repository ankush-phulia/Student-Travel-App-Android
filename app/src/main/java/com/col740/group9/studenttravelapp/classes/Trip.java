package com.col740.group9.studenttravelapp.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Trip extends Travel{
    public String trip_id;
    public String duration;
    public String budget;
    public String admin;

    public Trip(JSONObject trip_object) throws JSONException {
        this.trip_id = trip_object.getString("trip_id");
        this.start_time = trip_object.getString("start_time");
        this.duration = trip_object.getString("duration");
        this.source = trip_object.getString("source");
        this.destination = trip_object.getString("destination");
        this.budget = trip_object.getString("budget");
        this.admin = trip_object.getString("admin");

        JSONArray checkpoints = trip_object.getJSONArray("checkpoints");
        for (int i = 0; i < checkpoints.length(); i++) {
            this.checkpoints.add(
                    new JourneyPoint(checkpoints.getJSONObject(i)));
        }

        JSONArray participants = trip_object.getJSONArray("participants");
        for (int i = 0; i < participants.length(); i++) {
            this.participants.add(
                    new User(participants.getJSONObject(i)));
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("trip_id", this.trip_id);
        json.put("start_time", this.start_time);
        json.put("duration", this.duration);
        json.put("source", this.source);
        json.put("destination", this.destination);
        json.put("budget", this.budget);
        json.put("admin", this.admin);
        json.put("checkpoints", new JSONArray(this.checkpoints));
        json.put("participants", new JSONArray(this.participants));
        return json;
    }
}
