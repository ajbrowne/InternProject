package com.example.specialsapp.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;


public class MainActivity extends Activity {

    private JSONObject request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TheGPS gps = new TheGPS(this);

        LoginFragment fragment = new LoginFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "loginFragment");
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

    /*
       Fires the AuthRequest and then takes action based on result
    */
    public void asyncCheck(String user, String pass, String name, boolean check){
        AuthRequestAsync run = new AuthRequestAsync();
        String signUp = "0";
        int result = 0;

        if (check == true){
            signUp = "1";
        }

        try{
            result = run.execute(user, pass, name, signUp).get();
        } catch (InterruptedException e){
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }

        if (result == 0){
            if (check == false){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Invalid username or password")
                        .setMessage("Your username or password is incorrect, try again.")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onRestart();
                            }
                        }).show();
            }
        } else if(result == 1){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
    /**
     * Created by brownea on 6/12/14.
     * Builds HttpPost and sends JSON for login to api - returns the auth result code
     */
    public class AuthRequestAsync extends AsyncTask<String, Void, Integer> {

        private ProgressDialog dialog;
        HttpPost httpPost;
        JSONObject auth;

        @Override
        protected Integer doInBackground(String... params){
            String user = params[0];
            String pass = params[1];
            String name = params[2];
            String signUp = params[3];
            int authCode = 0;

            // Create http client
            HttpClient httpClient = new DefaultHttpClient();

            HttpHost proxy = new HttpHost("det-maharb-m", 8080, "http");
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);


            if (signUp.compareTo("0") == 0){
                // Create http post
                httpPost = new HttpPost(
                        "http://det-brownea-m:8080/v1/specials/login");
               auth = new JSONObject();
                try{
                    auth.put("username", user);
                    auth.put("password", pass);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else{
                // Create http post
                httpPost = new HttpPost(
                        "http://det-brownea-m:8080/v1/specials/register");
                auth = new JSONObject();
                try{
                    auth.put("username", user);
                    auth.put("password", pass);
                    auth.put("role", 1);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }


            String message = auth.toString();

            // Url encoding the POST parameters
            try{
                httpPost.setEntity(new StringEntity(message, "UTF8"));
                httpPost.setHeader("Content-type", "application/json");
            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

            // Make http request
            try{
                HttpResponse response = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();

                for (String line = null; (line = reader.readLine()) != null;){
                    builder.append(line).append("/n");
                }

                System.out.println(builder.toString());

                request = new JSONObject(builder.toString());

                Log.d("HTTP Response: ", response.toString());

                if (response.getStatusLine().getStatusCode() == 200){
                    System.out.println("GOT HERE!");
                    authCode = 1;
                }
            } catch (ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
            return authCode;
        }

    }

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        try {
            SHAHash = convertToHex(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //  result.setText("SHA-1 hash generated is: " + " " + SHAHash);
        return SHAHash;
    }

    private static String convertToHex(byte[] data) throws java.io.IOException {


        StringBuffer sb = new StringBuffer();
        String hex = null;

        hex = Base64.encodeToString(data, 0, data.length, Base64.NO_CLOSE);

        sb.append(hex);

        return sb.toString();
    }

    public void loadSavedPreferences() {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        String sUser = shared.getString("User", "");
        String sPass = shared.getString("Password", "");
        boolean check = shared.getBoolean("stored", false);
        if (check) {
            asyncCheck(sUser, sPass, "", check);
        }


    }

    public void savePreferences(String key, String value) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor edit = shared.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public void savePreferences(String key, boolean value) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor edit = shared.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }


}
