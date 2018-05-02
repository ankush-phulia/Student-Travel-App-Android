package com.col740.group9.studenttravelapp.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Journey extends Travel {
    public String journey_id;
    public String destination;

    public Journey() {

    }

    public Journey(JSONObject journey_object) throws JSONException, ParseException {
        this.journey_id = journey_object.getString("journey_id");

        String datestring = journey_object.getString("start_time");
        this.date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(datestring);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy 'at' HHmm 'hours'");
        this.display_time = df.format(date);

        this.start_time = journey_object.getString("start_time");
        this.source = journey_object.getString("source");
        this.destination = journey_object.getString("destination");
        this.cotravel_number = Integer.parseInt(journey_object.getString("cotravel_number"));
        this.posted = journey_object.getBoolean("posted");
        this.closed = journey_object.getBoolean("closed");

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

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.start_time = df.format(this.date);
        json.put("start_time", this.start_time);

        json.put("source", this.source);
        json.put("destination", this.destination);
        json.put("cotravel_number", this.cotravel_number);
        json.put("posted", false);
        json.put("closed", false);

        JSONArray checkpoints = new JSONArray();
        for (JourneyPoint checkpoint : this.checkpoints) {
            checkpoints.put(checkpoint.toJson());
        }
        json.put("checkpoints", checkpoints);

        JSONArray participants = new JSONArray();
        for (User user : this.participants) {
            JSONObject userJSON = user.toJSON().getJSONObject("user");
            userJSON.put("username", user.username);
            userJSON.put("id", user.id);
            participants.put(userJSON);
        }
        json.put("participants", participants);

        return json;
    }
}
