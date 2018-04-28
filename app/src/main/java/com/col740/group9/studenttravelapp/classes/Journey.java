package com.col740.group9.studenttravelapp.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Journey extends Travel{
    public String journey_id;

    public Journey(JSONObject journey_object) throws JSONException {
        this.journey_id = journey_object.getString("journey_id");
        this.start_time = journey_object.getString("start_time");
        this.start_date = journey_object.getString("start_date");
        this.duration = journey_object.getString("duration");
        this.source = journey_object.getString("source");
        this.destination = journey_object.getString("destination");

        JSONArray checkpoints = journey_object.getJSONArray("checkpoints");
        for (int i = 0; i < checkpoints.length(); i++) {
            this.checkpoints.add(
                    new JourneyPoint(checkpoints.getJSONObject(i)));
        }

        JSONArray participants = journey_object.getJSONArray("participants");
        for (int i = 0; i < participants.length(); i++) {
            this.participants.add(
                    new User(participants.getJSONObject(i)));
        }
    }
}
