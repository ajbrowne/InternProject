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
 * Hosts all fragments that deal with logging in and signing up.
 */
public class MainActivity extends BaseActivity {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private User user;

    /**
     * onCreate called when activity is created. This gets all needed views and carries
     * out necessary initializations.
     *
     * @param savedInstanceState -
     */
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

    /**
     * onCreateOptionsMenu gets the correct xml for the menu among other setup.
     *
     * @param menu - the menu for the activity
     * @return - true upon success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * onOptionsSelected handles action bar behavior for an activity.
     *
     * @param item - the selected item
     * @return - true upon success
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Http/Async call used for logging in with provided username and password.
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
            auth.put(USERNAME, username);
            auth.put(PASSWORD, password);
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
                        Log.d("MainActivity", "Error on login attempt");
                    }
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            Log.d("MainActivity", "Error on login attempt");
        }
    }

    /**
     * Http/Async call used for registering with provided user credentials.
     *
     * @param username - desired username
     * @param password - desired password
     * @param phone    - user phone number
     * @param zip      - user zip code
     * @param first    - user first name
     * @param last     - user last name
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
                        savePreferences("PASSWORD", Pass);
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            Log.e("SpecialsApp", "Error registering user");
        }
    }

    /**
     * Creates the JSON Object used for registration with the POST request.
     *
     * @param username - desired username
     * @param password - password entered
     * @param phone    - user phone number
     * @param zip      - user zip code
     * @param first    - user first name
     * @param last     - user last name
     * @param auth     - JSON Object returned with parameters
     * @throws JSONException
     */
    private void createAuth(String username, String password, String phone, String zip, String first, String last, JSONObject auth) throws JSONException {
        auth.put(USERNAME, username);
        auth.put(PASSWORD, password);
        auth.put("role", 1);
        auth.put("phone", phone);
        auth.put("zip", zip);
        auth.put("firstName", first);
        auth.put("lastName", last);
    }

    /**
     * First step of password encryption
     *
     * @param password - the password to be encrypted
     * @return - the encrypted string
     */
    public String computeSHAHash(String password) {
        String SHAHash = "ZZ";
        MessageDigest mdSha1;
        try {
            mdSha1 = MessageDigest.getInstance("SHA-1");
            mdSha1.update(password.getBytes("ASCII"));
            byte[] data = mdSha1.digest();
            SHAHash = convertToHex(data);
        } catch (NoSuchAlgorithmException | IOException e) {
            Log.e("SpecialsApp", "Error initializing SHA1 message digest");
        }

        return SHAHash;
    }

    /**
     * Converts to hex for encryption
     *
     * @param data - byte[] being converted
     * @return - the encrypted password
     * @throws java.io.IOException
     */
    private static String convertToHex(byte[] data) throws java.io.IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String hex;
        hex = Base64.encodeToString(data, 0, data.length, Base64.NO_CLOSE);
        stringBuilder.append(hex);
        return stringBuilder.toString();
    }

    /**
     * Store user for cached login, storing a string
     *
     * @param key   - key to retrieve value
     * @param value - value to be retrieved
     */
    private void savePreferences(String key, String value) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = shared.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * Store user for cached login, storing a boolean
     *
     * @param key   - key to retrieve value
     * @param value - value to be retrieved
     */
    private void savePreferences(String key, boolean value) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = shared.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * Controls the back stack correctly for signing up and logging in.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Returns the current user
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }


}
