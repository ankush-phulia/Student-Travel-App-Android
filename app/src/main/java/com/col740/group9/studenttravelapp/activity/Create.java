package com.col740.group9.studenttravelapp.activity;

import static com.col740.group9.studenttravelapp.classes.Constants.JOURNEY_TRAVEL_TYPE;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.classes.GlobalRequestQueue;
import com.col740.group9.studenttravelapp.fragment.CreateJourneyFragment;
import com.col740.group9.studenttravelapp.fragment.CreateTripFragment;

public class Create extends AppCompatActivity
        implements CreateJourneyFragment.OnCreateJourneyFragmentInteractionListener,
                CreateTripFragment.OnCreateTripFragmentInteractionListener {

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
    protected void onResume() {
        Fragment CreateFragment = null;
        if (traveltype == JOURNEY_TRAVEL_TYPE) {
            CreateFragment = new CreateJourneyFragment();
            getSupportActionBar().setTitle("Create Journey");
        } 
        else {
            CreateFragment = new CreateTripFragment();
            getSupportActionBar().setTitle("Create Trip");
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.create_contraintlayout, CreateFragment)
                .addToBackStack(null)
                .commit();

        mQueue = GlobalRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        mToken = (String) getIntent().getExtras().get("token");
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void OnCreateJourneyFragmentInteraction(Uri uri) {}

    @Override
    public void OnCreateTripFragmentInteraction(Uri uri) {}
}
