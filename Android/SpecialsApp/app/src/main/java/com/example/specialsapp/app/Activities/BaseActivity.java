package com.example.specialsapp.app.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.specialsapp.app.AlertDialogs.CustomAlertDialog;
import com.example.specialsapp.app.R;

/**
 * Base Activity extended by any activity that requires the normal menu for the app.
 */
public class BaseActivity extends FragmentActivity {

    private Menu menu;
    protected static final String FONT_PATH = "fonts/roboto-light.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        // Check login status, change menu appropriately
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loggedIn = shared.getBoolean("stored", true);

        menu.findItem(R.id.action_logout).setVisible(loggedIn);
        menu.findItem(R.id.action_login).setVisible(!loggedIn);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("User", "");
                edit.putString("Password", "");
                edit.putBoolean("stored", false);
                edit.commit();
                menu.findItem(R.id.action_logout).setVisible(false);
                menu.findItem(R.id.action_login).setVisible(true);
                new CustomAlertDialog(this, "Logout", "You have been logged out. You can no longer send contact info to dealers automatically").show();
                return true;
            case R.id.action_login:
                Intent newIntent = new Intent(this, MainActivity.class);
                startActivity(newIntent);
            case R.id.search:
                return false;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
