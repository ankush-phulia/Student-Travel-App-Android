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

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("journey_id", this.journey_id);
        json.put("start_time", this.start_time);
        json.put("source", this.source);
        json.put("destination", this.destination);
        json.put("checkpoints", new JSONArray(this.checkpoints));
        json.put("participants", new JSONArray(this.participants));
        return json;
    }
}
