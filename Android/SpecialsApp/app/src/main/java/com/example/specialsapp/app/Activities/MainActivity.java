package com.example.specialsapp.app.Activities;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.specialsapp.app.Fragments.LoginFragment;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.Rest.SpecialsRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Hosts all fragments that deal with logging in and signing up
 */
public class MainActivity extends FragmentActivity {

    private String zip;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create and show login fragment
        LoginFragment fragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "initial");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        return super.onOptionsItemSelected(item);
    }

    public void login(String username, String password) {

        final Boolean loggedIn;
        final String pass = password;
        final String user = username;
        JSONObject auth = new JSONObject();

        try {
            auth.put("username", username);
            auth.put("password", password);
            StringEntity entity = new StringEntity(auth.toString());
            SpecialsRestClient.post(this, "login", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,JSONObject request) {
                    try {
                        String response = request.getString("response");
                        if (response.compareTo("Login Success") == 0) {
                            savePreferences("stored", true);
                            savePreferences("User", user);
                            savePreferences("Password", pass);
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void register(String username, String password, String phone, String zip, String first, String last) {

        final Boolean loggedIn;
        final String pass = password;
        final String user = username;
        JSONObject auth = new JSONObject();

        try {
            auth.put("username", username);
            auth.put("password", password);
            auth.put("role", 1);
            auth.put("phone", phone);
            auth.put("zip", zip);
            auth.put("firstName", first);
            auth.put("lastName", last);
            StringEntity entity = new StringEntity(auth.toString());
            SpecialsRestClient.post(this, "register", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject request) {
                    if (statusCode == 201) {
                        savePreferences("stored", true);
                        savePreferences("User", user);
                        savePreferences("Password", pass);
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /*
        Used for password hashing
     */
    public String computeSHAHash(String password) {
        String SHAHash = "ZZ";
        MessageDigest mdSha1 = null;
        try {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            Log.e("myapp", "Error initializing SHA1 message digest");
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        try {
            SHAHash = convertToHex(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SHAHash;
    }

    // Converts to hex for encryption
    private static String convertToHex(byte[] data) throws java.io.IOException {

        StringBuffer sb = new StringBuffer();
        String hex = null;

        hex = Base64.encodeToString(data, 0, data.length, Base64.NO_CLOSE);

        sb.append(hex);

        return sb.toString();
    }

    /*
        Used to check for login and allow login caching
     */
    public void loadSavedPreferences() {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        String sUser = shared.getString("User", "");
        String sPass = shared.getString("Password", "");
        boolean check = shared.getBoolean("stored", false);
        if (check) {
            login(sUser, sPass);
        }
    }

    /*
        Stores user for cached login
     */
    public void savePreferences(String key, String value) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = shared.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /*
        Stores user for cached login
     */
    public void savePreferences(String key, boolean value) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = shared.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /*
        Properly controls backstack for signup and login fragments
     */
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    // Getters and setters used for signup
    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getZip() {
        return zip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
