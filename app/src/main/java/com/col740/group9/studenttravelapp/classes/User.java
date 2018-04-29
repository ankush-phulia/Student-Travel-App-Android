package com.col740.group9.studenttravelapp.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public int id;
    public String username;
    public String first_name;
    public String last_name;
    public String sex;
    public String email;
    public String password;
    public String facebook_link;

    public User(JSONObject user_info_json) throws JSONException {
        this.id = user_info_json.getInt("id");
        this.username = user_info_json.getString("username");
        this.first_name = user_info_json.getString("first_name");
        this.last_name = user_info_json.getString("last_name");
        this.email = user_info_json.getString("email");
        this.password = user_info_json.getString("password");
        this.sex = user_info_json.getString("sex");
        this.facebook_link = user_info_json.getString("facebook_link");
    }

    public void updateUser(JSONObject user_info_json) throws JSONException {
        this.id = user_info_json.getInt("id");
        this.username = user_info_json.getString("username");
        this.first_name = user_info_json.getString("first_name");
        this.last_name = user_info_json.getString("last_name");
        this.email = user_info_json.getString("email");
        this.password = user_info_json.getString("password");
        this.sex = user_info_json.getString("sex");
        this.facebook_link = user_info_json.getString("facebook_link");
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("username", this.username);
        json.put("first_name", this.first_name);
        json.put("last_name", this.last_name);
        json.put("email", this.email);
        json.put("password", this.password);
        json.put("sex", this.sex);
        json.put("facebook_link", this.facebook_link);
        return json;
    }
}
