package com.col740.group9.studenttravelapp.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Trip extends Travel{
    public String trip_id;
    public String budget;
    public String admin;

    public Trip(JSONObject trip_object) throws JSONException {
        this.trip_id = trip_object.getString("trip_id");
        this.start_time = trip_object.getString("start_time");
        this.start_date = trip_object.getString("start_date");
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
}
