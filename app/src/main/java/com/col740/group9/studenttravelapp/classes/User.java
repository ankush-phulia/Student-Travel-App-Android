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
    public String phone;
    public String bio;
    public String photo;

    public User(JSONObject user_info_json) throws JSONException {
        if (user_info_json.has("user")) {
            JSONObject user_json = user_info_json.getJSONObject("user");
            this.id = user_json.getInt("id");
            this.username = user_json.getString("username");
            this.first_name = user_json.getString("first_name");
            this.last_name = user_json.getString("last_name");
            this.email = user_json.getString("email");
            if (user_json.has("password")) {
                this.password = user_info_json.getString("password");
            }
        }
        else {
            this.id = user_info_json.getInt("id");
            this.username = user_info_json.getString("username");
            this.first_name = user_info_json.getString("first_name");
            this.last_name = user_info_json.getString("last_name");
            this.email = user_info_json.getString("email");
            if (user_info_json.has("password")) {
                this.password = user_info_json.getString("password");
            }
        }
        if (user_info_json.has("sex")) {
            this.sex = user_info_json.getString("sex");
        }
        if (user_info_json.has("facebook_link")) {
            this.facebook_link = user_info_json.getString("facebook_link");
        }
        if (user_info_json.has("phone")) {
            this.phone = user_info_json.getString("phone");
        }
        if (user_info_json.has("bio")) {
            this.bio = user_info_json.getString("bio");
        }
        if (user_info_json.has("photo")) {
            this.photo = user_info_json.getString("photo");
        }
    }

    public void updateUser(JSONObject user_info_json) throws JSONException {
        JSONObject user_json = user_info_json.getJSONObject("user");
        // this.id = user_json.getInt("id");
        // this.username = user_json.getString("username");
        this.first_name = user_json.getString("first_name");
        this.last_name = user_json.getString("last_name");
        this.email = user_json.getString("email");
        if (user_json.has("password")) {
            this.password = user_json.getString("password");
        }
        this.sex = user_info_json.getString("sex");
        this.facebook_link = user_info_json.getString("facebook_link");
        this.phone = user_info_json.getString("phone");
        this.bio = user_info_json.getString("bio");
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject user = new JSONObject();
        // user.put("id", this.id);
        // user.put("username", this.username);
        user.put("first_name", this.first_name);
        user.put("last_name", this.last_name);
        user.put("email", this.email);
        // user.put("password", this.password);
        json.put("user", user);
        json.put("sex", this.sex);
        json.put("facebook_link", this.facebook_link);
        json.put("phone", this.phone);
        json.put("bio", this.bio);
        return json;
    }
}
