package com.col740.group9.studenttravelapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.fragment.CreateTravelFragment;
import com.col740.group9.studenttravelapp.fragment.DashboardFragment;
import com.col740.group9.studenttravelapp.fragment.NotificationsFragment;
import com.col740.group9.studenttravelapp.fragment.UserProfileFragment;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    DashboardFragment.OnFragmentInteractionListener,
                    UserProfileFragment.OnFragmentInteractionListener,
                    NotificationsFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateTravelFragment createTravel = new CreateTravelFragment();
                createTravel.show(getFragmentManager(), "NEW_TRAVEL");
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_fragment_container, DashboardFragment.newInstance("", ""))
                .addToBackStack("DASHBOARD").commit();
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
            fragment = DashboardFragment.newInstance("", "");
        } else if (id == R.id.nav_user_profile) {
            tag = "PROFILE";
            fragment = UserProfileFragment.newInstance("", "");
        } else if (id == R.id.nav_notifications) {
            tag = "NOTIFICATIONS";
            fragment = NotificationsFragment.newInstance("", "");
        } else if (id == R.id.nav_logout) {
            logout();
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
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        return;
    }
}
