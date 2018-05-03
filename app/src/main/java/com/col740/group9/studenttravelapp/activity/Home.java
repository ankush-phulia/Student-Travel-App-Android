package com.col740.group9.studenttravelapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.classes.GlobalRequestQueue;
import com.col740.group9.studenttravelapp.fragment.DashboardFragment;
import com.col740.group9.studenttravelapp.fragment.DashboardJourneyFragment;
import com.col740.group9.studenttravelapp.fragment.DashboardTripFragment;
import com.col740.group9.studenttravelapp.fragment.NotificationsFragment;
import com.col740.group9.studenttravelapp.fragment.UserProfileFragment;

import static com.col740.group9.studenttravelapp.classes.Constants.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DashboardFragment.OnDashboardFragmentInteractionListener,
        DashboardJourneyFragment.OnDashboardJourneyFragmentInteractionListener,
        DashboardTripFragment.OnDashboardTripFragmentInteractionListener,
        UserProfileFragment.OnUserProfileFragmentInteractionListener,
        NotificationsFragment.OnNotificationsFragmentInteractionListener,
        Response.Listener<JSONArray>,
        Response.ErrorListener {

    public RequestQueue mQueue;
    public String mToken = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment DefaultFragment = null;
        String tag = "";

        // TODO set DefaulFragment as UserProfileFragment if new user else DashboardFragment
        DefaultFragment = new DashboardFragment();
        tag = "DASHBOARD";

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_fragment_container, DefaultFragment)
                .addToBackStack(tag).commit();
        toolbar.setTitle("Dashboard");
    }

    @Override
    protected void onResume() {

        mQueue = GlobalRequestQueue
                .getInstance(this.getApplicationContext())
                .getRequestQueue();
        mToken = (String) getIntent().getExtras().get("token");
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
//            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        String tag = "";
        if (id == R.id.nav_dashboard) {
            tag = "DASHBOARD";
            fragment = new DashboardFragment();
            toolbar.setTitle("Dashboard");
        } else if (id == R.id.nav_user_profile) {
            tag = "PROFILE";
            fragment = new UserProfileFragment();
            toolbar.setTitle("User Profile");
        } else if (id == R.id.nav_notifications) {
            tag = "NOTIFICATIONS";
            fragment = new NotificationsFragment();
            toolbar.setTitle("Notifications");
        } else if (id == R.id.nav_logout) {
            logout();
            return true;
        }

        if (!findFragmentinStack(tag)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment_container, fragment)
                    .addToBackStack(tag)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean findFragmentinStack(String className) {
        return getSupportFragmentManager().popBackStackImmediate(className, 0);
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logging out")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent gotoHome = new Intent(Home.this, Login.class);
                        startActivity(gotoHome);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    public void fetchDatafromServer(String type) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,
                        serverURL + "/" + type + "/",
                        null,
                        Home.this, Home.this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token " + mToken);
                return headers;
            }
        };
        mQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResponse(JSONArray response) {
        try {
            String type = "";
            if (response.length() > 0) {
                JSONObject firstElement = (JSONObject) response.get(0);
                if (firstElement.has("journey_id")) {
                    type = "journeys";
                } else if (firstElement.has("trip_id")) {
                    type = "trips";
                } else if (firstElement.has("user_from")) {
                    type = "notifications";
                } else if (firstElement.has("username")) {
                    type = "user_info";
                }
            }
            // customised processing per response
            switch (type) {
                case "journeys":
                    Log.w("Journeys", response.toString());
                    break;
                case "trips":
                    Log.w("Trips", response.toString());
                    break;
                case "notifications":
                    Log.w("Notifications", response.toString());
                    break;
                case "user_info":
                    Log.w("User Info", response.toString());
                    break;
                default:
                    Log.w("Default Home Response", response.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }

    @Override
    public void onDashboardFragmentInteraction(Uri uri) {

    }

    @Override
    public void onNotificationsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onUserProfileFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDashboardJourneyFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDashboardTripFragmentInteraction(Uri uri) {

    }
}