package com.example.specialsapp.app.Async;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by brownea on 6/12/14.
 * Builds HttpPost and sends JSON for login to api - returns the auth result code
 */
public class AuthAsyncTask extends AsyncTask<String, Void, Integer> {

    private JSONObject request;
    private HttpPost httpPost;
    private JSONObject auth;
    private HttpHost proxy;
    private HttpClient httpClient;

    public AuthAsyncTask(String type) {
        if (type.compareTo("login") == 0) {
            httpPost = new HttpPost(
                    "http://det-brownea-m:8080/v1/specials/login");
        } else {
            httpPost = new HttpPost(
                    "http://det-brownea-m:8080/v1/specials/register");
        }
        auth = new JSONObject();
        httpClient = new DefaultHttpClient();
        proxy = new HttpHost("det-brownea-m", 8080, "http");
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    @Override
    protected Integer doInBackground(String... params) {
        String user = params[0];
        String pass = params[1];
        String type = params[2];
        String first = params[3];
        String last = params[4];
        String zip = params[5];
        String phone = params[6];

        int authCode = 0;

        if (type.compareTo("login") == 0) {
            try {
                auth.put("username", user);
                auth.put("password", pass);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                auth.put("username", user);
                auth.put("password", pass);
                auth.put("role", 1);
                auth.put("phone", phone);
                auth.put("zip", zip);
                auth.put("firstName", first);
                auth.put("lastName", last);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        String message = auth.toString();
        System.out.println("BUILT: " + message);

        // Url encoding the POST parameters
        try {
            httpPost.setEntity(new StringEntity(message, "UTF8"));
            httpPost.setHeader("Content-type", "application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Make http request
        try {
            HttpResponse response = httpClient.execute(httpPost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();

            for (String line = null; (line = reader.readLine()) != null; ) {
                builder.append(line).append("/n");
            }

            System.out.println(builder.toString());

            //request = new JSONObject(builder.toString());

            Log.d("HTTP Response: ", response.toString());

            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
                authCode = 1;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authCode;
    }

}
