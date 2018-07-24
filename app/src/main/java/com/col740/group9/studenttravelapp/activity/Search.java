package com.col740.group9.studenttravelapp.activity;

import static com.col740.group9.studenttravelapp.classes.Constants.JOURNEY_TRAVEL_TYPE;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.classes.GlobalRequestQueue;
import com.col740.group9.studenttravelapp.fragment.SearchJourneyFragment;
import com.col740.group9.studenttravelapp.fragment.SearchTripFragment;

public class Search extends AppCompatActivity
        implements SearchJourneyFragment.OnSearchJourneyFragmentInteractionListener,
                SearchTripFragment.OnSearchTripFragmentInteractionListener {

    private int traveltype;
    public RequestQueue mQueue;
    public String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        traveltype = getIntent().getIntExtra("type", 0);
    }

    @Override
    protected void onResume() {
        Fragment SearchFragment = null;
        if (traveltype == JOURNEY_TRAVEL_TYPE) {
            SearchFragment = new SearchJourneyFragment();
            getSupportActionBar().setTitle("Search Journey");
        } else {
            SearchFragment = new SearchTripFragment();
            getSupportActionBar().setTitle("Search Trip");
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.search_contraintlayout, SearchFragment)
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
    public void OnSearchJourneyFragmentInteraction(Uri uri) {}

    @Override
    public void OnSearchTripFragmentInteraction(Uri uri) {}
}
