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
import org.json.JSONArray;
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
public class LocationAsyncTask extends AsyncTask<Double, Void, Integer> {

    private JSONArray request;
    private HttpPost httpPost;
    private JSONObject auth;
    private HttpHost proxy;
    private HttpClient httpClient;
    private JSONObject location;
    private JSONObject point;
    private JSONArray coord;

    public LocationAsyncTask() {
        httpPost = new HttpPost(
                "http://det-brownea-m:8080/v1/specials/dealers");
        auth = new JSONObject();
        httpClient = new DefaultHttpClient();
        proxy = new HttpHost("det-brownea-m", 8080, "http");
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        location = new JSONObject();
        point = new JSONObject();
        coord = new JSONArray();
    }

    @Override
    protected Integer doInBackground(Double... params) {
        Double latitude = params[0];
        Double longitude = params[1];
        int authCode = 0;

        try {
            point.put("type", "Point");
            coord.put(longitude);
            coord.put(latitude);
            point.put("coordinates", coord);
            location.put("loc", point);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String message = location.toString();
        System.out.println(message.toString());

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

            request = new JSONArray(builder.toString());

            Log.d("HTTP Response: ", response.toString());

            if (response.getStatusLine().getStatusCode() == 200) {
                authCode = 1;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return authCode;
    }
}
