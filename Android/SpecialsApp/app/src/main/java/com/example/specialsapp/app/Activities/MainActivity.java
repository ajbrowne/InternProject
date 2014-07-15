package com.example.specialsapp.app.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.specialsapp.app.Fragments.LoginFragment;
import com.example.specialsapp.app.Models.User;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.Rest.SpecialsRestClient;
import com.example.specialsapp.app.SignUpFragments.SignupNumberFragment;
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
public class MainActivity extends BaseActivity {

    private static final String Username = "username";
    private static final String Password = "password";
    private User user;

    /**
     * @param data -
     * @return -
     * @throws java.io.IOException
     */
    private static String convertToHex(byte[] data) throws java.io.IOException {

        StringBuilder stringBuilder = new StringBuilder();
        String hex;

        hex = Base64.encodeToString(data, 0, data.length, Base64.NO_CLOSE);

        stringBuilder.append(hex);

        return stringBuilder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        user = new User();
        boolean check = getIntent().getBooleanExtra("submit", false);
        if (check) {
            SignupNumberFragment signupNumberFragment = new SignupNumberFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, signupNumberFragment, "number");
            fragmentTransaction.commit();
        } else {
            // Create and show login fragment
            LoginFragment fragment = new LoginFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, fragment, "initial");
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Http/Async call used for logging in with provided username and password
     *
     * @param username - email address entered
     * @param password - password entered
     */
    public void login(String username, String password) {

        final String Pass = password;
        final String User = username;
        JSONObject auth = new JSONObject();

        // Async call using Async HTTP Android client posting JSON for login
        try {
            auth.put(Username, username);
            auth.put(Password, password);
            StringEntity entity = new StringEntity(auth.toString());
            SpecialsRestClient.post(this, "login", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject request) {
                    try {
                        // Check response for success and cache user login
                        String response = request.getString("response");
                        if (response.compareTo("Login Success") == 0) {
                            savePreferences("stored", true);
                            savePreferences("User", User);
                            savePreferences("Password", Pass);
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

    /**
     * Http/Async call used for registering with provided user credentials
     *
     * @param username -
     * @param password -
     * @param phone    -
     * @param zip      -
     * @param first    -
     * @param last     -
     */
    public void register(String username, String password, String phone, String zip, String first, String last) {

        final String Pass = password;
        final String User = username;
        JSONObject auth = new JSONObject();

        try {
            createAuth(username, password, phone, zip, first, last, auth);
            StringEntity entity = new StringEntity(auth.toString());
            SpecialsRestClient.post(this, "register", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject request) {
                    if (statusCode == 201) {
                        savePreferences("stored", true);
                        savePreferences("User", User);
                        savePreferences("Password", Pass);
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

    private void createAuth(String username, String password, String phone, String zip, String first, String last, JSONObject auth) throws JSONException {
        auth.put(Username, username);
        auth.put(Password, password);
        auth.put("role", 1);
        auth.put("phone", phone);
        auth.put("zip", zip);
        auth.put("firstName", first);
        auth.put("lastName", last);
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

    /*
        Stores user for cached login
     */
    private void savePreferences(String key, String value) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = shared.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /*
        Stores user for cached login
     */
    private void savePreferences(String key, boolean value) {
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
        super.onBackPressed();
    }

    public User getUser() {
        return user;
    }


}
