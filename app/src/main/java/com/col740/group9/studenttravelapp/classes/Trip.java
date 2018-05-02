package com.col740.group9.studenttravelapp.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Trip extends Travel{
    public String trip_id;
    public String trip_info;
    public String duration;
    public String expected_budget;

    public Trip(){

    }

    public Trip(JSONObject trip_object) throws JSONException,ParseException {
        this.start_time = trip_object.getString("start_time");
        this.source = trip_object.getString("source");
        this.duration = trip_object.getString("duration");
        this.trip_id = trip_object.getString("trip_id");
        this.trip_info = trip_object.getString("trip_info");

        String datestring = trip_object.getString("start_time");
        this.date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(datestring);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy 'at' HHmm 'hours'");
        this.display_time = df.format(date);

        JSONArray participants = trip_object.getJSONArray("participants");
        for (int i = 0; i < participants.length(); i++) {
            this.participants.add(
                    new User(participants.getJSONObject(i)));
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("trip_id", this.trip_id);
        json.put("trip_info", this.trip_info);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.start_time = df.format(this.date);
        json.put("start_time", this.start_time);

        json.put("source", this.source);
        json.put("duration", this.duration);
        json.put("expected_budget", this.expected_budget);
        json.put("posted", false);
        json.put("closed", false);
        json.put("checkpoints", new JSONArray(this.checkpoints));
        json.put("participants", new JSONArray(this.participants));

        return json;
    }
}
