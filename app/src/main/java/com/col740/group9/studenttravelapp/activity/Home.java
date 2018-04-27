package com.col740.group9.studenttravelapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.col740.group9.studenttravelapp.R;

import com.col740.group9.studenttravelapp.classes.GlobalRequestQueue;
import com.col740.group9.studenttravelapp.fragment.CreateTravelFragment;
import com.col740.group9.studenttravelapp.fragment.DashboardFragment;
import com.col740.group9.studenttravelapp.fragment.NotificationsFragment;
import com.col740.group9.studenttravelapp.fragment.UserProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    DashboardFragment.OnDashboardFragmentInteractionListener,
                    UserProfileFragment.OnUserProfileFragmentInteractionListener,
                    NotificationsFragment.OnNotificationsFragmentInteractionListener{

    final String server = "http://10.0.2.2:8000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume(){
        Fragment DefaultFragment = null;
        String tag = "";

        // TODO if new account is created then UserProfileFragment else DashboardFragment
        DefaultFragment = new DashboardFragment();
        tag = "DASHBOARD";

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_fragment_container, DefaultFragment)
                .addToBackStack(tag)
                .commit();

        try {
            fetchDetails();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onResume();
    }

    public void fetchDetails() throws JSONException {
        final RequestQueue queue = GlobalRequestQueue.getInstance(this.getApplicationContext()).
                getRequestQueue();

        final JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", "ankush");
        jsonBody.put("password", "ankushphulia");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, server + "/api-token-auth/", jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final String token = (String) response.get("token");
                            System.out.println(' ');
                            System.out.println(token);
                            final JSONObject jsonBody = new JSONObject();

                            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                                    (Request.Method.GET, server + "/notifications/", null, new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            try {
                                                System.out.println("Received" + response.toString());
                                            }
                                            catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                        }
                                    }){
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    HashMap<String, String> headers = new HashMap<String, String>();
                                    headers.put("Authorization", "Token " + token);
                                    return headers;
                                }
                            };
                            queue.add(jsonObjectRequest);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(jsonObjectRequest);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

        }
        else {
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
        }
        else if (id == R.id.action_logout){
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
        } else if (id == R.id.nav_user_profile) {
            tag = "PROFILE";
            fragment = new UserProfileFragment();
        } else if (id == R.id.nav_notifications) {
            tag = "NOTIFICATIONS";
            fragment = new NotificationsFragment();
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
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
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

    @Override
    public void onDashboardFragmentInteraction(Uri uri) {

    }

    @Override
    public void onNotificationsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onUserProfileFragmentInteraction(Uri uri) {

    }
}
