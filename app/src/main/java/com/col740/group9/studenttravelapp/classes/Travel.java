package com.col740.group9.studenttravelapp.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Travel {
    public String travel_id;
    public String start_time;
    public String source;
    public String destination;
    public ArrayList<JourneyPoint> checkpoints = new ArrayList<JourneyPoint>();
    public ArrayList<User> participants = new ArrayList<User>();
}
