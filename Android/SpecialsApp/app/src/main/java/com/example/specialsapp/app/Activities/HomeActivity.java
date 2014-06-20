package com.example.specialsapp.app.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.specialsapp.app.AlertDialogs.CustomAlertDialog;
import com.example.specialsapp.app.Async.LocationAsyncTask;
import com.example.specialsapp.app.Fragments.NearbyDealersFragment;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.R;

import java.util.concurrent.ExecutionException;

/**
 * Hosts all fragments that display dealers and their specials
 */
public class HomeActivity extends Activity {

    Double lat;
    Double longi;
    Menu menu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mMenuList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mMenuList = getResources().getStringArray(R.array.list_items);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
       // mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mMenuList));

        // Get location upon opening app, returning to Dealers
        GPS gps = new GPS(this);
        lat = gps.getLatitude();
        longi = gps.getLongitude();
        asyncCheck(lat, longi);

        // Show NearbyDealersFragment
        NearbyDealersFragment nearbyDealersFragment = new NearbyDealersFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer2, nearbyDealersFragment, "nearby");
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        // Check login status, change menu appropriately
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        boolean status = shared.getBoolean("stored", true);
        if (status) {
            menu.findItem(R.id.action_logout).setVisible(true);
            menu.findItem(R.id.action_login).setVisible(false);
        } else {
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_login).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = shared.edit();
            edit.putString("User", "");
            edit.putString("Password", "");
            edit.putBoolean("stored", false);
            edit.commit();
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_login).setVisible(true);
            new CustomAlertDialog(this, "Logout", "You have been logged out. You can no longer send contact info to dealers").show();
            return true;
        }
        if (id == android.R.id.home) {
            NearbyDealersFragment nearbyDealersFragment = new NearbyDealersFragment();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer2, nearbyDealersFragment);
            fragmentTransaction.commit();
        }
        if (id == R.id.action_login) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /*
       Fires the LocationAsyncTask after login and then takes action based on result
    */
    public void asyncCheck(Double latitude, Double longitude) {
        LocationAsyncTask run = new LocationAsyncTask();
        int result = 0;

        try {
            result = run.execute(latitude, longitude).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (result == 0) {
            //new CustomAlertDialog(this, "Invalid username or password", "Your username or password is incorrect, try again.").show();
        } else if (result == 1) {
            // Do stuff with results from Mongo
        }
    }

    // Controls backstack for dealers/specials fragments
    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

}
