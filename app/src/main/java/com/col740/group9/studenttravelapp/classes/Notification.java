package com.col740.group9.studenttravelapp.classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Notification {
    public String title;
    public String description;
    public String type;
    public String date;

    public Notification(JSONObject notification_json) throws JSONException,ParseException {
        this.title = notification_json.getString("title");
        this.description = notification_json.getString("description");
        this.type = notification_json.getString("notif_type");
        String datestring = notification_json.getString("creation_time");
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(datestring);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy 'at' HHmm 'hours'");
        this.date = df.format(date);
    }
}
