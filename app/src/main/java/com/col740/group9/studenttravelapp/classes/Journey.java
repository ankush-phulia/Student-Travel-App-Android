package com.col740.group9.studenttravelapp.classes;

import java.util.ArrayList;

public class Journey {
    public String journey_id;
    public String start_time;
    public String source;
    public String destination;
    public ArrayList<JourneyPoint> checkpoints = new ArrayList<JourneyPoint>();
    public ArrayList<User> participants = new ArrayList<User>();
}
