package com.col740.group9.studenttravelapp.classes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class Notification {
    public String id;
    public String title;
    public String description;
    public String type;
    public String date;
    public String resolved;

    public Notification(JSONObject notification_json) throws JSONException, ParseException {
        this.id = notification_json.getString("id");
        this.title = notification_json.getString("title");
        this.description = notification_json.getString("description");
        this.type = notification_json.getString("notif_type");
        String datestring = notification_json.getString("creation_time");
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(datestring);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy 'at' HHmm 'hours'");
        this.date = df.format(date);
        this.resolved = notification_json.getString("resolved");
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        return json;
    }
}
