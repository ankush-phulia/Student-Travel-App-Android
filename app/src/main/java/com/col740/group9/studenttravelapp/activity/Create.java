package com.col740.group9.studenttravelapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.col740.group9.studenttravelapp.R;

public class Create extends AppCompatActivity {

    private int traveltype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        traveltype = getIntent().getIntExtra("type",0);
    }
}