package com.col740.group9.studenttravelapp.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class Notification {
    public String title;
    public String description;
    public String type;
    public String date;

    public Notification(JSONObject notification_json) throws JSONException {
        this.title = notification_json.getString("title");
        this.description = notification_json.getString("description");
        this.type = notification_json.getString("notif_type");
        this.date = notification_json.getString("creation_time");
    }
}
