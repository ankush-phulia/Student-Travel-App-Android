package com.col740.group9.studenttravelapp.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Journey extends Travel{
    public String journey_id;
    public int cotrael_number;

    public Journey(JSONObject journey_object) throws JSONException,ParseException {
        this.journey_id = journey_object.getString("journey_id");
        this.start_time = journey_object.getString("start_time");
        String datestring = journey_object.getString("start_time");
        this.date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(datestring);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy 'at' HHmm 'hours'");
        this.display_time = df.format(date);
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
