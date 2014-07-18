package com.example.specialsapp.app.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.specialsapp.app.AlertDialogs.CustomAlertDialog;
import com.example.specialsapp.app.R;

public class SettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        addPreferencesFromResource(R.xml.pref_general); //TODO don't use deprecated methods
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();//TODO don't use deprecated methods
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        EditTextPreference editTextPref = (EditTextPreference) findPreference("zip_code");

        if (!sharedPreferences.getString("zip_code", "").equals("")) {
            editTextPref.setSummary(sharedPreferences.getString("zip_code", "Enter Zip Code"));
        }

        boolean useLocation = sharedPreferences.getBoolean("use_location", false);
        editTextPref.setEnabled(!useLocation);
        editTextPref.setSelectable(true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);//TODO don't use deprecated methods
        if (pref instanceof EditTextPreference) { //TODO Don't use instanceof, if possible. Instead of instanceof, use Java OO (interfaces)
            EditTextPreference etp = (EditTextPreference) pref;
            pref.setSummary(etp.getText());
        }
        if (pref instanceof CheckBoxPreference) {
            EditTextPreference editTextPref = (EditTextPreference) findPreference("zip_code");

            boolean useLocation = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("use_location", false);
            editTextPref.setEnabled(!useLocation);
            editTextPref.setSelectable(true);
        }
    }

    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()//TODO don't use deprecated methods
                .registerOnSharedPreferenceChangeListener(this);
    }

    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);

        // Check login status, change menu appropriately
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        boolean status = shared.getBoolean("stored", true);
        menu.findItem(R.id.action_logout).setVisible(status);
        menu.findItem(R.id.action_login).setVisible(!status);
        return super.onCreateOptionsMenu(menu);
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
            new CustomAlertDialog(this, "Logout", "You have been logged out. " +
                    "You can no longer send contact info to dealers").show();
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (id == R.id.action_login) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
