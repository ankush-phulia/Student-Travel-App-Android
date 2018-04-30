package com.col740.group9.studenttravelapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.classes.GlobalRequestQueue;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.col740.group9.studenttravelapp.classes.Constants.serverURL;

public class Create extends AppCompatActivity {

    private int traveltype;
    public RequestQueue mQueue;
    public String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        traveltype = getIntent().getIntExtra("type", 0);
        setContentView(R.layout.activity_create);
    }

    @Override
    protected void onResume(){

        mQueue = GlobalRequestQueue
                .getInstance(this.getApplicationContext())
                .getRequestQueue();
        mToken = (String) getIntent().getExtras().get("token");
        super.onResume();
    }

}
