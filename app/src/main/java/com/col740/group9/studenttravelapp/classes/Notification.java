package com.col740.group9.studenttravelapp.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class Notification {
    public String title;
    public String detail;
    public String type;
    public String date;

    public Notification(JSONObject notification_json) throws JSONException {
        this.detail = notification_json.getString("title");
        this.detail = notification_json.getString("detail");
        this.type = notification_json.getString("notif_type");
        this.date = notification_json.getString("creation_time");
    }
}
